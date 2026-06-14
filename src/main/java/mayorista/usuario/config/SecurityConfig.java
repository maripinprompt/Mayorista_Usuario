package mayorista.usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // indica que esta clase tiene configuraciones de Spring
@EnableWebSecurity // activa Spring Security en la aplicación
public class SecurityConfig {

    @Bean // Spring gestiona este objeto automáticamente
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desactivamos CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // swagger sin autenticación
                .anyRequest().authenticated() // cualquier otro endpoint requiere autenticación
            )
            .httpBasic(basic -> {}); // autenticación básica con usuario y contraseña
        return http.build();
    }
}