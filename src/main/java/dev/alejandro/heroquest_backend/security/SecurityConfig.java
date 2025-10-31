package dev.alejandro.heroquest_backend.security;

import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuración de seguridad principal de la aplicación.
 * 
 * - Permite registro y login sin autenticación.
 * - Requiere autenticación (Basic o Bearer JWT) para el resto de endpoints.
 * - Habilita generación y validación de tokens JWT simétricos (HS512).
 * - Usa JpaUserDetailsService para autenticar usuarios reales desde MySQL.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.key}")
    private String key;

    // ✅ FIX 1: hacemos opcional el "endpoint" en caso de que falte en application.properties
    @Value("${api-endpoint:/api}")
    private String endpoint;

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS habilitado (para llamadas desde el frontend)
            .cors(cors -> cors.configurationSource(corsConfiguration()))

            // Desactivamos CSRF (no usamos sesiones ni formularios web)
            .csrf(csrf -> csrf.disable())

            // Permitir acceso a la consola H2 en modo dev
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

            // 🔓 Endpoints públicos
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()

                // ✅ FIX 2: añadimos rutas absolutas sin depender del endpoint variable
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                // Mantiene compatibilidad si endpoint está configurado correctamente
                .requestMatchers(HttpMethod.POST, endpoint + "/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, endpoint + "/auth/login").permitAll()

                // Generar token requiere Basic Auth (usuario válido)
                .requestMatchers(HttpMethod.POST, endpoint + "/auth/token").authenticated()

                // ✅ FIX 3: permitimos OPTIONS (preflight CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Todo lo demás necesita autenticación
                .anyRequest().authenticated()
            )

            // Autenticación básica habilitada (necesaria para /auth/token)
            .httpBasic(withDefaults())

            // JWT: el servidor validará tokens en endpoints protegidos
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))

            // Sin sesiones: cada request se valida con su token
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Cargar usuarios desde BD (UserDetailsService)
            .userDetailsService(jpaUserDetailsService);

        return http.build();
    }

    /**
     * Encoder JWT: genera tokens firmados con clave simétrica (HS512)
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key.getBytes()));
    }

    /**
     * Decoder JWT: valida tokens JWT entrantes con la misma clave
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(bytes, 0, bytes.length, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    /**
     * PasswordEncoder: encripta contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de CORS (permite peticiones desde el frontend local)
     */
    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}