package Libreria.controladores;

import Libreria.entidades.Autor;
import Libreria.entidades.Editorial;
import Libreria.entidades.Libro;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.AutorRepositorio;
import Libreria.repositorios.EditorialRepositorio;
import Libreria.repositorios.LibroRepositorio;
import Libreria.servicios.LibroServicio;
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
public class LibroControlador {
    
    @Autowired
    private LibroServicio libroservicio;
    @Autowired
    private AutorRepositorio autorrepositorio;
    @Autowired
    private EditorialRepositorio editorialrepositorio;
    @Autowired
    private LibroRepositorio librorepositorio;
    
    @GetMapping("/libro")
    public String libros(ModelMap modelo){
        
        modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
        modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        modelo.put("libros",librorepositorio.listarLibrosOrdenadosNombre());
        modelo.put("alta", "SI");
        modelo.put("baja", "NO");
        
        return "libros.html";
    }
    
    @PostMapping("/registrarlibros")
    public String registrarlibros(ModelMap modelo, @RequestParam(required = false) String titulo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) String idautor, @RequestParam(required = false) String ideditorial){
        try {
            libroservicio.registrarLibro(isbn, titulo, anio, ejemplares, 0, ejemplares, idautor, ideditorial);
            modelo.put("exito", "El libro se registró correctamente");
            
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
            modelo.put("libros",librorepositorio.listarLibrosOrdenadosNombre());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
            
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("isbn", isbn);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
            modelo.put("libros",librorepositorio.listarLibrosOrdenadosNombre());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        } catch (NumberFormatException e){
            modelo.put("error", "No debe colocar valores excesivos");
        }
            
        return "libros.html";      // RedirectView("/")Me derirecciona a la raíz del html
                                   // (colocar en el retorno del método)  
    }
    
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id){
        try {
            libroservicio.darBajaLibro(id);
            return "redirect:/libro";
        } catch (Exception ex) {
            return "redirect:/libro";
        }
    }
    
    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id){
        try {
            libroservicio.darAltaLibro(id);
            return "redirect:/libro";
        } catch (Exception ex) {
            return "redirect:/libro";
        }
    }
    
    @GetMapping("/listalibros")
    public String listarlibros(ModelMap modelo){
        modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
        modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        modelo.put("libros",librorepositorio.listarLibrosOrdenadosNombre());
        return "listalibros.html";
    }
    
    @GetMapping("/modificarlibros/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        
        modelo.put("libro", librorepositorio.getOne(id));
        modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
        modelo.put("editoriales", editorialrepositorio.buscarEditorialOrdenada());
        
        return "librosmodificar.html";
    }
    
    @PostMapping("/modificarlibros/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id, @RequestParam(required = false) String titulo, @RequestParam(required = false) Long isbn, @RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) String idautor, @RequestParam(required = false) String ideditorial){
        try {
            libroservicio.modificarLibro(id, isbn, titulo, anio, ejemplares, 0, ejemplares, idautor, ideditorial);
            modelo.put("exito", "El libro se modificó correctamente");
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
            
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
        }
        
        return "listactualizada.html";
    }
    
    @GetMapping("/eliminarlibros/{id}")
    public String eliminar(ModelMap modelo, @PathVariable String id){
        try {
            System.out.println("dentro del método eliminar");
            libroservicio.eliminarLibro(id);
            return "redirect:/listalibros";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("libros", librorepositorio.listarLibrosOrdenadosNombre());
            return "listactualizada.html";
        }
    }
}
