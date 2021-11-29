    package Libreria.servicios;

import Libreria.entidades.Autor;
import Libreria.entidades.Libro;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.AutorRepositorio;
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
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Transactional //La anotacion transactional indica que el método realiza modificaciones en la base de datos
    public void registrarAutor(String nombre) throws ErrorServicio{
        validarDatos(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        
        autorRepositorio.save(autor); //El repositorio se encarga de crear las tablas y guardar el autor en la base de datos
    }
    
    @Transactional
    public void modificarAutor(String id, String nombre) throws ErrorServicio{
        validarDatos(nombre);
        
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if(respuesta.isPresent()){
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            
            autorRepositorio.save(autor);
        } else{
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }
    
    @Transactional
    public void bajaAutor(String id) throws ErrorServicio{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if(respuesta.isPresent()){
            Autor autor = respuesta.get();
            autor.setAlta(false);
            
            autorRepositorio.save(autor);
        } else{
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }
    
    @Transactional
    public void altaAutor(String id) throws ErrorServicio{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if(respuesta.isPresent()){
            Autor autor = respuesta.get();
            autor.setAlta(true);
            
            autorRepositorio.save(autor);
        } else{
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }
    
    @Transactional
    public void eliminarAutor(String id) throws ErrorServicio{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if(respuesta.isPresent()){
            Autor autor = respuesta.get();
            if(autorRepositorio.validarAutorLibro(id).size() == 0){
                autorRepositorio.delete(autor);
            } else{
                throw new ErrorServicio("No puede eliminar autores que tengan libros asociados");
            }
        } else{
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
                
    }
    
    private void validarDatos(String nombre) throws ErrorServicio{
        if(nombre == null || nombre.trim().isEmpty()){
            throw new ErrorServicio("El nombre del autor no puede ser nulo");
        }
    }
}
