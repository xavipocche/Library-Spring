package Libreria.servicios;

import Libreria.entidades.Libro;
import Libreria.entidades.Autor;
import Libreria.entidades.Editorial;
import Libreria.entidades.Prestamo;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.AutorRepositorio;
import Libreria.repositorios.EditorialRepositorio;
import Libreria.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Xavier Pocchettino
 */
@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired 
    private AutorRepositorio autorRepositorio;
    @Autowired 
    private EditorialRepositorio editorialRepositorio;
    
    private Date fechaactual = new Date();
    
    @Transactional //La anotacion transactional indica que el método realiza modificaciones en la base de datos
    public void registrarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String idautor, String ideditorial) throws ErrorServicio{
        
        validarID(idautor, ideditorial);
        
        Autor autor = autorRepositorio.getOne(idautor);
        Editorial editorial = editorialRepositorio.getOne(ideditorial);
        
        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
        
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(libro.getEjemplares());
        libro.setAlta(true);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        libroRepositorio.save(libro); //El repositorio se encarga de crear las tablas y guardar el libro en la base de datos
    }
    
    @Transactional
    public void modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String idautor, String ideditorial) throws ErrorServicio{
        validarID(idautor, ideditorial);
        
        Autor autor = autorRepositorio.getOne(idautor);
        Editorial editorial = editorialRepositorio.getOne(ideditorial);
            
        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
        Optional<Libro> respuesta = libroRepositorio.findById(id); //El find del repositorio nos devuelve un objeto de la clase optional el cual es posible validar en el siquiente if
            
        if(respuesta.isPresent()){
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados());
            libro.setEjemplaresRestantes(libro.getEjemplares()-libro.getEjemplaresPrestados());
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libroRepositorio.save(libro);
        } else{
            throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }
    
    @Transactional
    public void darBajaLibro(String id) throws ErrorServicio{
        Optional<Libro> respuesta = libroRepositorio.findById(id); 
        if(respuesta.isPresent()){
            Libro libro = respuesta.get();
            libro.setAlta(false);
            
            libroRepositorio.save(libro);
            
        } else{
           throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }
    
    @Transactional
    public void darAltaLibro(String id) throws ErrorServicio{
        Optional<Libro> respuesta = libroRepositorio.findById(id); 
        if(respuesta.isPresent()){
            Libro libro = respuesta.get();
            libro.setAlta(true);
            
            libroRepositorio.save(libro);
        } else{
           throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }
    
    @Transactional
    public void eliminarLibro(String id) throws ErrorServicio{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if(respuesta.isPresent()){
            Libro libro = respuesta.get();
            if(libroRepositorio.validarLibroPrestamo(id).size() == 0){
                libroRepositorio.delete(libro);
            } else{
                throw new ErrorServicio("No puede eliminar libros que tengan préstamos asociados");
            }
        } else{
            throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }
    
    private void validarDatos(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Autor autor, Editorial editorial) throws ErrorServicio{
        if(titulo == null || titulo.isEmpty()){
            throw new ErrorServicio("El título del libro no puede ser nulo");
        }
        if(titulo.length() > 40){
            throw new ErrorServicio("El título del libro no puede contener más de 40 caracteres (" + (titulo.length()) + ")");
        }
        if(isbn == null){
            throw new ErrorServicio("El ISBN no puede ser nulo");
        }
        if(isbn < 0){
            throw new ErrorServicio("El ISBN del libro no puede ser negativo");
        }
        if(isbn > 999999999999999999L){
            throw new ErrorServicio("El ISBN del libro debe ser un número menor");
        }
        if(anio == null){
            throw new ErrorServicio("El año del libro no puede ser nulo");
        }
        if(anio < 1900){
            throw new ErrorServicio("El año del libro no debe ser inferior a 1900");
        }
        if(ejemplares == null){
            throw new ErrorServicio("La cantidad de ejemplares no puede estar vacía");
        }
        if(ejemplares < 0){
            throw new ErrorServicio("La cantidad de ejemplares no puede ser negativa");
        }
        if(ejemplaresPrestados > ejemplares){
            throw new ErrorServicio("La cantidad de libros prestados no puede exceder la cantidad de libros");
        }
        if(autor == null){
            throw new ErrorServicio("No existe un autor con el id identificado");
        }
        if(editorial == null){
            throw new ErrorServicio("No existe una editorial con el id indentificado");
        }
        if(anio > fechaactual.getYear()+1900){
            throw new ErrorServicio("El año del libro no puede ser mayor al año actual");
        }
    }
    
    private void validarID(String idautor, String ideditorial) throws ErrorServicio{
        if(idautor == null || idautor.trim().isEmpty()){
            throw new ErrorServicio("El libro debe tener un autor asignado, si no hay, registre uno");
        }
        if(ideditorial == null || ideditorial.trim().isEmpty()){
            throw new ErrorServicio("EL libro debe tener una editorial asignada, si no hay, registre una");
        }
    }
}
