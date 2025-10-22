package dev.alejandro.heroquest_backend.security;

import dev.alejandro.heroquest_backend.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Adaptador de nuestra entidad User al modelo de seguridad de Spring Security.
 * Implementa UserDetails para que Spring Security pueda autenticar y autorizar usuarios.
 */
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * En esta versión inicial, todos los usuarios tienen el rol ROLE_USER por defecto.
     * Más adelante, si añadimos roles, se poblará dinámicamente desde la base de datos.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // no manejamos expiración de cuentas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // no manejamos bloqueo
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // las credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // todas las cuentas están activas
    }
}