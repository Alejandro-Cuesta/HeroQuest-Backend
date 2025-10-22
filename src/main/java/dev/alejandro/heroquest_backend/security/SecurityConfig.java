package dev.alejandro.heroquest_backend.security;

import dev.alejandro.heroquest_backend.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig
 *
 * - Habilita CORS con una configuración explícita (origen del frontend).
 * - Desactiva CSRF para APIs REST (en desarrollo).
 * - Permite acceso público a /api/auth/** y a la consola H2 (/h2-console/**).
 * - Marca la sesión como STATELESS (preparado para JWT).
 * - Registra PasswordEncoder (BCrypt).
 * NOTA: Este fichero delega en JpaUserDetailsService para cargar usuarios desde la BD.
 *       También está preparado para añadir un filtro JWT (JwtAuthenticationFilter)
 *       en una fase posterior.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Origen del frontend — cámbialo si tu frontend corre en otro host/puerto
    @Value("${frontend.origin:http://localhost:5173}")
    private String frontendOrigin;

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    /**
     * Define la cadena de filtros de seguridad.
     * - Permite /api/auth/** (register/login) sin autenticación.
     * - Permite la consola H2 en desarrollo.
     * - Protege el resto de endpoints requiriendo autenticación.
     * - SessionCreationPolicy.STATELESS para uso de tokens (JWT).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF desactivado para APIs (si usas cookies con refresh token, revisaremos CSRF)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
            // Habilita que la consola H2 funcione en iframe (solo en dev)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            // Desactiva login por formulario; no queremos pages de Spring Security
            .formLogin(form -> form.disable())
            // Logout básico (puedes personalizar más adelante)
            .logout(logout -> logout.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
            // Autorizar peticiones
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()           // registro y login públicos
                .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll() // endpoints públicos opcionales
                .anyRequest().authenticated() // resto requiere autenticación
            )
            // Indica que usaremos nuestro UserDetailsService
            .userDetailsService(jpaUserDetailsService)
            // No usamos sesiones del servidor para autenticar (usaremos JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Deshabilita basic auth por defecto (se puede quitar si se quiere activar)
            .httpBasic(Customizer.withDefaults());

        // Si más tarde añado un filtro JWT, lo insertare aquí:
        // http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * BCrypt password encoder bean para encriptar y comprobar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración CORS global: orígenes, métodos, cabeceras y credenciales.
     * Ajusta allowedOrigins si tu frontend está hospedado en otra URL.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true); // necesario si usamos cookies
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}