package Libreria.controladores;

import Libreria.repositorios.LibroRepositorio;
import Libreria.repositorios.UsuarioRepositorio;
import Libreria.servicios.UsuarioServicio;
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
@Controller
@RequestMapping("/")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @GetMapping("/")
    public String login(ModelMap modelo, @RequestParam(required = false) String error, @RequestParam(required = false) String exito){
        if(error != null){
            modelo.put("error", "Nombre de usuario o clave incorrectos");
        }
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo){
        modelo.put("libros", libroRepositorio.listarLibrosOrdenadosNombre());
        return "inicio.html";
    }
            
    @GetMapping("/registro")
    public String registro(){
        return "usuarioregistro.html";
    }
    
    @PostMapping("/registro")
    public String registrarUsuario(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String password, @RequestParam String password2){
        try {
            usuarioServicio.registrarUsuario(nombre, apellido, email, password, password2);
            modelo.put("exito", "Registro exitoso");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("password", password);
            modelo.put("password2", password2);
        }
        
        return "usuarioregistro.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/useregistroprestamo")
    public String registrarPrestamo(ModelMap modelo){
        modelo.put("libros", libroRepositorio.listarLibrosOrdenadosNombre());
        return "prestamousuario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuario")
    public String usuarios(ModelMap modelo){
        modelo.put("usuarios", usuarioRepositorio.listarUsuariosOrdenados());
        modelo.put("alta", "SI");
        modelo.put("baja", "NO");
        
        return "usuarios.html";
    }
    
    @GetMapping("bajausuario/{id}")
    public String baja(@PathVariable String id){
        try {
            usuarioServicio.bajaUsuario(id);
            return "redirect:/usuario";
        } catch (Exception ex) {
            return "redirect:/usuario";
        }
    } 
    
    @GetMapping("altausuario/{id}")
    public String alta(@PathVariable String id){
        try {
            usuarioServicio.altaUsuario(id);
            return "redirect:/usuario";
        } catch (Exception e) {
            return "redirect:/usuario";
        }
    }
    
            
}
