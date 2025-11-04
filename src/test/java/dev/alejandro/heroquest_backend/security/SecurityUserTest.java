package dev.alejandro.heroquest_backend.security;

import dev.alejandro.heroquest_backend.auth.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUserTest {

    @Test
    void securityUser_ShouldReturnCorrectValues() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("secret")
                .build();

        SecurityUser securityUser = new SecurityUser(user);

        assertEquals("testuser", securityUser.getUsername());
        assertEquals("secret", securityUser.getPassword());
        assertTrue(securityUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        assertTrue(securityUser.isAccountNonExpired());
        assertTrue(securityUser.isAccountNonLocked());
        assertTrue(securityUser.isCredentialsNonExpired());
        assertTrue(securityUser.isEnabled());
    }
}