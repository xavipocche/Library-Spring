package Libreria.security;

import Libreria.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author Xavier Pocchettino
 */

@Configuration 
@EnableWebSecurity 
@EnableGlobalMethodSecurity(prePostEnabled=true) 
@Order(1)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Override 
    protected void configure(HttpSecurity http) throws Exception {http
            .authorizeRequests() 
                .antMatchers("/css/*", "/js/*","/img/*", "/**").permitAll()
            
            .and().formLogin() 
                .loginPage("/") // Dirección donde está mi login
                .loginProcessingUrl("/logincheck") // URL para formulario en login 
                .usernameParameter("username") // Como viajan los datos del logueo
                .passwordParameter("password")// Como viajan los datos del logueo
                .defaultSuccessUrl("/inicio") // A que URL viaja cuando inicia sesión
            
            .and().logout() // Aca configuro la salida 
                .logoutUrl("/logout") 
                .logoutSuccessUrl("/") 
                .permitAll().and().csrf().disable();
    }
}