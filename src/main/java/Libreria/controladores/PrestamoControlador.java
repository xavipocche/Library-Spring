package Libreria.controladores;

import Libreria.repositorios.LibroRepositorio;
import Libreria.repositorios.UsuarioRepositorio;
import Libreria.repositorios.PrestamoRepositorio;
import Libreria.servicios.PrestamoServicio;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Xavier Pocchettino
 */
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/")
public class PrestamoControlador {
    
    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private PrestamoRepositorio prestamoRepositorio; 
    @Autowired
    private LibroRepositorio librorepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @GetMapping("/prestamo")
    public String prestamo(ModelMap modelo){
        modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorUsuarios());
        modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
        modelo.put("usuarios", usuarioRepositorio.listarUsuariosOrdenados());
        modelo.put("alta", "SI");
        modelo.put("baja", "NO");
        
        return "prestamo.html";
    }
    
    
    //CAMBIAR POR REGISTRAR USUARIO
    @PostMapping("/registrarprestamo")
    public String registrarprestamo(ModelMap modelo, @RequestParam String idusuario, @RequestParam(required = false) String fechaDevolucion, @RequestParam String idlibro){
        try {
            System.out.println(idusuario);
            if(fechaDevolucion == null || fechaDevolucion.trim().isEmpty()){
                throw new Exception("La fecha ingresada no puede ser nula");
            }
            
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDevolucion);
            
            prestamoServicio.registrarPrestamo(fecha, idlibro, idusuario);
            modelo.put("exito", "El préstamo se registró correctamente");
            
            modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorUsuarios());
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
            modelo.put("usuarios", usuarioRepositorio.listarUsuariosOrdenados());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
            
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorUsuarios());
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
            modelo.put("usuarios", usuarioRepositorio.listarUsuariosOrdenados());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        }
        return "prestamo.html";
    }
    
    //REGISTRO DE PRÉSTAMOS EXCLUSIVO PARA USUARIOS
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/registrarprestamousuario")
    public String registrarprestamousuario(ModelMap modelo, @RequestParam String idusuario, @RequestParam(required = false) String fechaDevolucion, @RequestParam String idlibro){
        try {
            System.out.println(idusuario + " ID DEL CLIENTE");
            if(fechaDevolucion == null || fechaDevolucion.trim().isEmpty()){
                throw new Exception("La fecha ingresada no puede ser nula");
            }
            
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDevolucion);
            
            prestamoServicio.registrarPrestamo(fecha, idlibro, idusuario);
            modelo.put("exito", "El préstamo se registró correctamente");
            
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
        }
        return "prestamousuario.html";
    }
    
    @GetMapping("/bajaprestamo/{id}")
    public String baja(@PathVariable String id){
        try {
            prestamoServicio.bajaPrestamo(id);
            return "redirect:/prestamo";
        } catch (Exception ex) {
            return "redirect:/prestamo";
        }
    }
    
    @GetMapping("/altaprestamo/{id}")
    public String alta(@PathVariable String id){
        try {
            prestamoServicio.altaPrestamo(id);
            return "redirect:/prestamo";
        } catch (Exception ex) {
            return "redirect:/prestamo";
        }
    }
    
    @GetMapping("/prestamolista")
    public String listarPrestamos(ModelMap modelo){
        modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorUsuarios());
        
        return "prestamolista.html";
    }
    
    @GetMapping("/prestamosmodificar/{id}")
    public String modificarPrestamos(ModelMap modelo, @PathVariable String id){
        
        modelo.put("prestamo", prestamoRepositorio.getOne(id));
        modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
        modelo.put("usuarios", usuarioRepositorio.listarUsuariosOrdenados());
        
        return "prestamomodificar.html";
    }
    
    @PostMapping("/prestamosmodificar/{id}")
    public String modificarPrestamos(ModelMap modelo, @PathVariable String id, @RequestParam(required = false) String fechaDevolucion, @RequestParam String idlibro, @RequestParam String idusuario){
        try {
            
            if(fechaDevolucion == null || fechaDevolucion.trim().isEmpty()){
                throw new Exception("La fecha no puede ser nula");
            }
            
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDevolucion);
            prestamoServicio.modificarPrestamo(id, fecha, idlibro, idusuario);
            modelo.put("exito", "El préstamo se actualizó correctamente");
            modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorTitulo());
            
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorTitulo());
        }
        return "/prestamolistactualizada";
    }
    
    @GetMapping("/prestamoseliminar/{id}")
    public String eliminarprestamo(ModelMap modelo, @PathVariable String id){
        try {
            prestamoServicio.eliminarPrestamo(id);
            return "redirect:/prestamolista";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("prestamos", prestamoRepositorio.buscarPrestamosPorTitulo());
            return "/prestamolistactualizada";
        }
    }
}
//AL EFECTUAR UN PRESTAMO RESTAR DEL STOCK DISPONIBLE DE LOS LIBROS