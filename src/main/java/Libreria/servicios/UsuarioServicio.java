package Libreria.servicios;

import Libreria.Roles;
import Libreria.entidades.Usuario;
import Libreria.errores.ErrorServicio;
import Libreria.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Xavier Pocchettino
 */
@Service
public class UsuarioServicio implements UserDetailsService { //Implementa UserDetailsService para conectarlo con SpringSecurity
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void registrarUsuario(String nombre, String apellido, String email, String password, String password2) throws ErrorServicio{
        validarDatos(nombre, apellido, email, password, password2);
        
        Usuario usuario = new Usuario();
        
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setRol(Roles.USER);
        usuario.setAlta(true);
        
        
        String encriptada = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encriptada);
        usuario.setPassword2(encriptada);
        
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void bajaUsuario(String ideusuario) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(ideusuario);
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setAlta(false);
            
            usuarioRepositorio.save(usuario);
        } else{
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }
    
    @Transactional
    public void altaUsuario(String ideusuario) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(ideusuario);
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setAlta(true);
            
            usuarioRepositorio.save(usuario);
        } else{
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }
    
    private void validarDatos(String nombre, String apellido, String email, String password, String password2) throws ErrorServicio{
        if(nombre == null || nombre.trim().isEmpty()){
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if(nombre.length() > 20){
            throw new ErrorServicio("El nombre no puede exceder de los 20 caracteres");
        }
        if(apellido == null || apellido.trim().isEmpty()){
            throw new ErrorServicio("El apellido no puede ser nulo"); 
        }
        if(apellido.length() > 20){
            throw new ErrorServicio("El apellido no puede exceder de los 20 caracteres");
        }
        if(email == null || email.trim().isEmpty()){
            throw new ErrorServicio("El email no puede ser nulo");
        }
        if(!email.contains("@")){
            throw new ErrorServicio("El email ingresado no es válido");
        }
        if(password.length() < 8){
            throw new ErrorServicio("La contraseña debe tener al menos 8 caracteres");
        }
        if(!password.equals(password2)){
            throw new ErrorServicio("Las contraseñas no coinciden");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if(usuario != null){
            List<GrantedAuthority> permisos = new ArrayList();
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);
            
            return user;
        } else{
            return null;
        }
    }
}
