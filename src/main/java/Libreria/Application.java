package Libreria;

import Libreria.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
// configureGlobal indica a la configuración de Spring cuál es el servicio que utilizaremos para autentificar el usuario y setea un encriptador de contraseñas
    @Autowired 
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }
}
