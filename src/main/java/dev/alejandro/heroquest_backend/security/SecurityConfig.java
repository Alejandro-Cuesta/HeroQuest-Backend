package dev.alejandro.heroquest_backend.security;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;

import dev.alejandro.heroquest_backend.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * SecurityConfig
 * - Usa autenticación básica (httpBasic) para emitir tokens JWT.
 * - Los tokens se validan con JwtDecoder y JwtEncoder simétricos (HS512).
 * - Las sesiones son STATELESS (no hay cookies de sesión).
 * - CORS habilitado para el frontend local.
 * - Integración con JpaUserDetailsService (usuarios reales desde MySQL).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.key}")
    private String key;

    @Value("${api-endpoint}")
    private String endpoint;

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS habilitado (frontend)
            .cors(cors -> cors.configurationSource(corsConfiguration()))

            // Desactivar CSRF (no usamos sesiones)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())

            // Permitir consola H2 (solo dev)
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

            // Autorizaciones específicas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, endpoint).permitAll()
                .requestMatchers(HttpMethod.POST, endpoint + "/auth/token").hasRole("USER")
                .requestMatchers(endpoint + "/private").access(hasScope("READ"))
                .anyRequest().access(hasScope("READ"))
            )

            // Stateless: sin sesiones en servidor
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // JWT decoder para validar tokens entrantes
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder())))

            // Basic Auth para generar token
            .httpBasic(withDefaults())

            // Carga usuarios desde la BD
            .userDetailsService(jpaUserDetailsService);

        return http.build();
    }

    /**
     * Bean JwtEncoder: genera tokens firmados HS512 (symmetric key).
     */
    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key.getBytes()));
    }

    /**
     * Bean JwtDecoder: valida tokens JWT con la misma clave simétrica.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(bytes, 0, bytes.length, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    /**
     * BCrypt encoder para contraseñas de usuarios reales.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración CORS (permite llamadas del frontend en localhost:5173)
     */
    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}