package dev.alejandro.heroquest_backend.auth.service;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.exception.InvalidCredentialsException;
import dev.alejandro.heroquest_backend.auth.exception.UserAlreadyExistsException;
import dev.alejandro.heroquest_backend.auth.mapper.UserMapper;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     *  Registro de nuevo jugador
     * Comprueba si el usuario ya existe.
     * Comprueba que las contraseñas coinciden.
     * Encripta la contraseña y guarda el nuevo usuario.
     * Devuelve un UserDTOResponse sin contraseña.
     */
    @Override
    public UserDTOResponse register(UserRegisterDTORequest dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        
        User user = userMapper.toUser(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDTOResponse(savedUser);
    }

    /**
     *  Login de jugador
     * Busca al usuario por email.
     * Verifica la contraseña con BCrypt.
     * Devuelve el DTO si las credenciales son correctas.
     */
    @Override
    public UserDTOResponse login(UserLoginDTORequest dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return userMapper.toUserDTOResponse(user);
    }
}