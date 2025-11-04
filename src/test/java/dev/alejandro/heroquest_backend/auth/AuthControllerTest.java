package dev.alejandro.heroquest_backend.auth;

import dev.alejandro.heroquest_backend.auth.controller.AuthController;
import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.service.AuthService;
import dev.alejandro.heroquest_backend.auth.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

    @Test
    void register_ShouldReturnCreated() throws Exception {
        UserRegisterDTORequest dto = new UserRegisterDTORequest();
        dto.setUsername("user@test.com");
        dto.setPassword("Password1");
        dto.setConfirmPassword("Password1");
        dto.setDisplayName("User");

        UserDTOResponse response = new UserDTOResponse();
        response.setUsername("user@test.com");

        when(authService.register(any(UserRegisterDTORequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@test.com\",\"password\":\"Password1\",\"confirmPassword\":\"Password1\",\"displayName\":\"User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user@test.com"));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        UserLoginDTORequest dto = new UserLoginDTORequest();
        dto.setUsername("user@test.com");
        dto.setPassword("Password1");

        UserDTOResponse response = new UserDTOResponse();
        response.setUsername("user@test.com");

        when(authService.login(any(UserLoginDTORequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@test.com\",\"password\":\"Password1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@test.com"));
    }

    @Test
    void token_ShouldReturnJwt() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user");
        when(tokenService.generateToken(auth)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/auth/token")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }
}