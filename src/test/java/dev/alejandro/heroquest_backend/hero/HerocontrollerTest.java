package dev.alejandro.heroquest_backend.hero;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.controller.HeroController;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.dto.HeroUpdateStatsDTO;
import dev.alejandro.heroquest_backend.hero.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeroController.class)
class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HeroService heroService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Jwt jwt;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("testuser");
    }

    @Test
    void createHero_ShouldReturnCreated() throws Exception {
        HeroDTORequest dto = new HeroDTORequest();
        dto.setHeroClass("Barbarian");

        // Builder sin puntosRestantes
        HeroDTOResponse response = HeroDTOResponse.builder()
                .id(1L)
                .heroClass("Barbarian")
                .health(12)
                .attack(14)
                .defense(8)
                .movement(6)
                .experiencia(0)
                .nivel(1)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(heroService.createHero(user, dto)).thenReturn(response);

        mockMvc.perform(post("/api/hero")
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.heroClass").value("Barbarian"))
                .andExpect(jsonPath("$.health").value(12));
    }

    @Test
    void getHero_ShouldReturnOk() throws Exception {
        HeroDTOResponse response = HeroDTOResponse.builder()
                .id(1L)
                .heroClass("Barbarian")
                .health(12)
                .attack(14)
                .defense(8)
                .movement(6)
                .experiencia(0)
                .nivel(1)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(heroService.getHeroByUser(user)).thenReturn(response);

        mockMvc.perform(get("/api/hero")
                .principal(() -> "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heroClass").value("Barbarian"))
                .andExpect(jsonPath("$.health").value(12));
    }

    @Test
    void updateHeroStats_ShouldReturnOk() throws Exception {
        HeroUpdateStatsDTO statsDTO = new HeroUpdateStatsDTO();
        statsDTO.setHealth(20);
        statsDTO.setDefense(15);
        statsDTO.setAttack(25);
        statsDTO.setMovement(7);
        // Ignoramos puntosRestantes en el test

        HeroDTOResponse response = HeroDTOResponse.builder()
                .id(1L)
                .heroClass("Barbarian")
                .health(20)
                .attack(25)
                .defense(15)
                .movement(7)
                .experiencia(0)
                .nivel(1)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(heroService.updateHeroStats(user, statsDTO)).thenReturn(response);

        mockMvc.perform(put("/api/hero/update-stats")
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health").value(20))
                .andExpect(jsonPath("$.attack").value(25))
                .andExpect(jsonPath("$.defense").value(15))
                .andExpect(jsonPath("$.movement").value(7));
    }
}