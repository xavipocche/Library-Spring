package Libreria.controladores;

import Libreria.entidades.Editorial;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.EditorialRepositorio;
import Libreria.servicios.EditorialServicio;
import java.util.List;
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
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialservicio;
    @Autowired
    private EditorialRepositorio editorialrepositorio;
    
    @GetMapping("/editoriales")
    public String editoriales(ModelMap modelo){
        modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        modelo.put("alta", "SI");
        modelo.put("baja", "NO");
        return "editoriales";
    }
    
    @PostMapping("/registrareditoriales")
    public String registrareditoriales(ModelMap modelo ,@RequestParam String nombre){
        try {
            editorialservicio.registrarEditorial(nombre);
            modelo.put("exito", "La editorial se registró correctamente");
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        }
        
        return "editoriales.html";
    }
    
    @GetMapping("/bajaeditorial/{id}")
    public String baja(@PathVariable String id){
        try {
            editorialservicio.bajaEditorial(id);
            return "redirect:/editoriales";
        } catch (Exception e) {
            return "redirect:/editoriales";
        }
        
    }
    
    @GetMapping("/altaeditorial/{id}")
    public String alta(@PathVariable String id){
        try {
            editorialservicio.altaEditorial(id);
            return "redirect:/editoriales";
        } catch (Exception e) {
            return "redirect:/editoriales";
        }
    }
    
    @GetMapping("/editorialeslista")
    public String listareditoriales(ModelMap modelo){
        modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        return "editorialeslista.html";
    }
    
    @GetMapping("/editorialesmodificar/{id}")
    public String modificareditoriales(ModelMap modelo, @PathVariable String id){
        modelo.put("editorial", editorialrepositorio.getOne(id));
        return "editorialmodificar.html";
    }
    
    @PostMapping("/editorialesmodificar/{id}")
    public String modificareditoriales(ModelMap modelo, @PathVariable String id, String nombre){
        try {
            editorialservicio.modificarEditorial(id, nombre);
            modelo.put("exito", "La editorial se actualizó correctamente");
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        }
        
        return "editorialeslistactualizada.html";
    }
    
    @GetMapping("/editorialeseliminar/{id}")
    public String eliminareditorial(ModelMap modelo, @PathVariable String id){
        try {
            editorialservicio.eliminarEditorial(id);
            return "redirect:/editorialeslista";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
            return "editorialeslistactualizada.html";
        }
    }
}
