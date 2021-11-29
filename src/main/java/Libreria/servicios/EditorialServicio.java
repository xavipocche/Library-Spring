package Libreria.servicios;

import Libreria.entidades.Editorial;
import Libreria.entidades.Libro;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.EditorialRepositorio;
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
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional //La anotacion transactional indica que el método realiza modificaciones en la base de datos
    public void registrarEditorial(String nombre) throws ErrorServicio{
        validarDatos(nombre);
        
        Editorial editorial = new Editorial();
        
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        
        editorialRepositorio.save(editorial); //El repositorio se encarga de crear las tablas y guardar la editorial en la base de datos
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws ErrorServicio{
        validarDatos(nombre);
        
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            
            editorialRepositorio.save(editorial);
        } else{
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }
    
    @Transactional
    public void bajaEditorial(String id) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            
            editorialRepositorio.save(editorial);
        } else{
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }
    
    @Transactional
    public void altaEditorial(String id) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);
            
            editorialRepositorio.save(editorial);
        } else{
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }
    
    @Transactional
    public void eliminarEditorial(String id) throws Exception{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            List<Libro> libros = editorialRepositorio.validarEditorialLibro(id);
            if(libros.size() == 0){
                editorialRepositorio.delete(editorial);
            } else{
                throw new ErrorServicio("No puede eliminar editoriales que tengan libros asociados");
            }
        } else{
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
                
    }
    
    private void validarDatos(String nombre) throws ErrorServicio{
        if(nombre == null || nombre.trim().isEmpty()){
            throw new ErrorServicio("El nombre de la editorial no puede ser nulo");
        }
    }
}
