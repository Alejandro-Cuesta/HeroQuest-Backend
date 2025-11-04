package dev.alejandro.heroquest_backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void h2Console_ShouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/hero"))
                .andExpect(status().isUnauthorized());
    }
}
