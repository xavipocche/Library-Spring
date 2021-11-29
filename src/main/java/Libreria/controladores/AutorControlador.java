package Libreria.controladores;

import Libreria.entidades.Autor;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.AutorRepositorio;
import Libreria.servicios.AutorServicio;
import java.util.ArrayList;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Xavier Pocchettino
 */
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/")
public class AutorControlador {
    
    @Autowired
    private AutorServicio autorservicio;
    @Autowired
    private AutorRepositorio autorrepositorio;
    
    @GetMapping("/autores")
    public String autor(ModelMap modelo){
        List<Autor> autores = autorrepositorio.buscarAutoresOrdenados();
        modelo.put("autores", autores);
        modelo.put("alta", "SI");
        modelo.put("baja", "NO");
        
        return "autores";
    }
    
    @PostMapping("/registrarautores")
    public String registrarautores(ModelMap modelo, @RequestParam String nombre){
        try {
            autorservicio.registrarAutor(nombre);
            modelo.put("exito", "El autor se registró correctamente");
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            modelo.put("alta", "SI");
            modelo.put("baja", "NO");
        }
        
        return "autores.html";
    }
    
    @GetMapping("/bajautor/{id}")
    public String baja(@PathVariable String id){
        try {
            autorservicio.bajaAutor(id);
            return "redirect:/autores";
        } catch (Exception ex) {
            return "redirect:/autores";
        }
    }
    
    @GetMapping("/altautor/{id}")
    public String alta(@PathVariable String id){
        try {
            autorservicio.altaAutor(id);
            return "redirect:/autores";
        } catch (Exception e) {
            return "redirect:/autores";
        }
    }
    
    @GetMapping("/autoreslista")
    public String listarautores(ModelMap modelo){
        modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
        return "autoreslista.html";
    }
    
    @GetMapping("/autoresmodificar/{id}")
    public String modificarautor(ModelMap modelo, @PathVariable String id){
        modelo.put("autor", autorrepositorio.getOne(id));
        return "autoresmodificar.html";
    }
    
    @PostMapping("/autoresmodificar/{id}")
    public String modificarautor(ModelMap modelo, @PathVariable String id, String nombre){
        try {
            autorservicio.modificarAutor(id, nombre);
            modelo.put("exito", "El autor se actualizó correctamente");
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            
        }
        
        return "autoreslistactualizada.html";
    }
    
    @GetMapping("/eliminarautor/{id}")
    public String eliminarautor(ModelMap modelo, @PathVariable String id){
        try {
            autorservicio.eliminarAutor(id);
            return "redirect:/autoreslista";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("autores", autorrepositorio.buscarAutoresOrdenados());
            return "autoreslistactualizada.html"; 
        }
    }
}
