package dev.alejandro.heroquest_backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_ShouldReturnCreated() throws Exception {
        UserRegisterDTORequest dto = new UserRegisterDTORequest();
        dto.setUsername("newuser");
        dto.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void loginUser_ShouldReturnToken() throws Exception {
        User user = User.builder().username("loginuser").password("$2a$10$7zC3FUpbKf0.1Yc2yIhV2uB7h5Lqv8g8Yw1cFT4r9rXyqXJZ2u1l6").build();
        userRepository.save(user);

        UserLoginDTORequest dto = new UserLoginDTORequest();
        dto.setUsername("loginuser");
        dto.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}