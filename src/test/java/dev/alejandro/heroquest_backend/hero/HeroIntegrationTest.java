package dev.alejandro.heroquest_backend.hero;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HeroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    private String token;
    private User user;

    @BeforeEach
    void setUp() {
        heroRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder().username("heroUser").password("password123").build();
        userRepository.save(user);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        //  CORRECCIÓN: usamos JwtEncoderParameters
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        token = jwt.getTokenValue();
    }

    @Test
    void createHero_ShouldReturnCreated() throws Exception {
        HeroDTORequest dto = new HeroDTORequest();
        dto.setHeroClass("Barbarian");

        mockMvc.perform(post("/api/hero")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.heroClass").value("Barbarian"));
    }

    @Test
    void getHero_ShouldReturnOk() throws Exception {
        createHero_ShouldReturnCreated();

        mockMvc.perform(get("/api/hero")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heroClass").value("Barbarian"));
    }

    
}