package dev.alejandro.heroquest_backend.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import dev.alejandro.heroquest_backend.inventory.dto.ItemDTORequest;
import dev.alejandro.heroquest_backend.inventory.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    private User user;
    private Hero hero;
    private String token;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        heroRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder().username("inventoryUser").password("password123").build();
        userRepository.save(user);

        hero = Hero.builder().heroClass("Barbarian").user(user).build();
        heroRepository.save(hero);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // CORRECCIÓN: usamos JwtEncoderParameters
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        token = jwt.getTokenValue();
    }

    @Test
    void addItem_ShouldReturnOk() throws Exception {
        ItemDTORequest dto = new ItemDTORequest();
        dto.setName("Sword");
        dto.setType("Weapon");
        dto.setBonusAttack(5);

        mockMvc.perform(post("/api/inventory/add")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sword"));
    }

    @Test
    void getItems_ShouldReturnOk() throws Exception {
        addItem_ShouldReturnOk();

        mockMvc.perform(get("/api/inventory")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sword"));
    }
}