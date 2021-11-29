package Libreria.servicios;

import Libreria.entidades.Libro;
import Libreria.entidades.Prestamo;
import Libreria.entidades.Usuario;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.LibroRepositorio;
import Libreria.repositorios.PrestamoRepositorio;
import Libreria.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Xavier Pocchettino
 */
@Service
public class PrestamoServicio {
    
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired 
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void registrarPrestamo(Date fechaDevolucion, String idlibro, String idusuario) throws ErrorServicio{
        validarID(idlibro, idusuario);
        
        Libro libro = libroRepositorio.getOne(idlibro);
        Usuario usuario = usuarioRepositorio.getOne(idusuario); //CAMBIAR POR USUARIO NO USAMOS MAS LOS CLIENTES
        
        validarDatos(fechaDevolucion, libro, usuario);
        
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        
        if(prestamo.getFechaPrestamo().before(fechaDevolucion)){
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setLibro(libro);
            prestamo.setUsuario(usuario);
            prestamo.setAlta(true);
        } else{
            throw new ErrorServicio("La fecha de devolución no puede ser anterior a la fecha actual");
        }
        prestamoRepositorio.save(prestamo);
    }
    
    @Transactional
    public void modificarPrestamo(String id, Date fechaDevolucion, String idlibro, String idusuario) throws ErrorServicio{
        validarID(idlibro, idusuario);
        
        Libro libro = libroRepositorio.getOne(idlibro);
        Usuario usuario = usuarioRepositorio.getOne(idusuario);
        
        validarDatos(fechaDevolucion, libro, usuario);
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if(respuesta.isPresent()){
            Prestamo prestamo = respuesta.get();
            
            if(prestamo.getFechaPrestamo().before(fechaDevolucion)){
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setLibro(libro);
            prestamo.setUsuario(usuario);
            prestamo.setAlta(true);
        
            } else{
                throw new ErrorServicio("La fecha de devolución no puede ser anterior a la fecha creación del préstamo");
            }
            
            prestamoRepositorio.save(prestamo);
            
        } else{
            throw new ErrorServicio("No se encontró el préstamo solicitado");
        }
    }  
    
    @Transactional
    public void bajaPrestamo(String id) throws ErrorServicio{
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if(respuesta.isPresent()){
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(false);
            
            prestamoRepositorio.save(prestamo);
        } else{
            throw new ErrorServicio("No se encontró el préstamo solicitado");
        }
    }
    
    @Transactional
    public void altaPrestamo(String id) throws ErrorServicio{
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if(respuesta.isPresent()){
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(true);
            
            prestamoRepositorio.save(prestamo);
        } else{
            throw new ErrorServicio("No se encontró el préstamo solicitado");
        }
    }
    
    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicio{
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if(respuesta.isPresent()){
            Prestamo prestamo = respuesta.get();
            
            prestamoRepositorio.delete(prestamo);
        } else{
            throw new ErrorServicio("No se encontró el préstamo solicitado");
        }
    }
    
    private void validarDatos(Date fechaDevolucion, Libro libro, Usuario usuario) throws ErrorServicio{
        if(fechaDevolucion == null){
            throw new ErrorServicio("La fecha de devolución no puede ser nula");
        }
        if(libro == null){
            throw new ErrorServicio("Debe seleccionar un libro en el préstamo");
        }
        if(usuario == null){
            throw new ErrorServicio("El préstamo debe tener un cliente asociado");
        }
    }
    
    private void validarID(String idlibro, String idusuario) throws ErrorServicio{
        if(idlibro == null || idlibro.trim().isEmpty()){
            throw new ErrorServicio("El préstamo debe tener un libro asignado, si no hay, registre uno");
        }
        if(idusuario == null || idusuario.trim().isEmpty()){
            throw new ErrorServicio("El préstamo debe tener un cliente asignado, si no hay, regisre uno");
        }
    }
}
