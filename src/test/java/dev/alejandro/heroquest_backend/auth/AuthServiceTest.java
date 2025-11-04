package dev.alejandro.heroquest_backend.auth;

import dev.alejandro.heroquest_backend.auth.dto.UserDTOResponse;
import dev.alejandro.heroquest_backend.auth.dto.UserLoginDTORequest;
import dev.alejandro.heroquest_backend.auth.dto.UserRegisterDTORequest;
import dev.alejandro.heroquest_backend.auth.exception.InvalidCredentialsException;
import dev.alejandro.heroquest_backend.auth.exception.UserAlreadyExistsException;
import dev.alejandro.heroquest_backend.auth.mapper.UserMapper;
import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.auth.repository.UserRepository;
import dev.alejandro.heroquest_backend.auth.service.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldThrow_WhenUserAlreadyExists() {
        UserRegisterDTORequest dto = new UserRegisterDTORequest();
        dto.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(dto));
    }

    @Test
    void register_ShouldThrow_WhenPasswordsDontMatch() {
        UserRegisterDTORequest dto = new UserRegisterDTORequest();
        dto.setUsername("newUser");
        dto.setPassword("1234");
        dto.setConfirmPassword("4321");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.register(dto));
    }

    @Test
    void register_ShouldReturnUserDTO_WhenSuccess() {
        UserRegisterDTORequest dto = new UserRegisterDTORequest();
        dto.setUsername("newUser");
        dto.setPassword("1234");
        dto.setConfirmPassword("1234");

        User userEntity = new User();
        userEntity.setUsername("newUser");

        User savedUser = new User();
        savedUser.setUsername("newUser");

        UserDTOResponse dtoResponse = new UserDTOResponse();
        dtoResponse.setUsername("newUser");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userMapper.toUser(dto)).thenReturn(userEntity);
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toUserDTOResponse(savedUser)).thenReturn(dtoResponse);

        UserDTOResponse result = authService.register(dto);
        assertEquals("newUser", result.getUsername());
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        UserLoginDTORequest dto = new UserLoginDTORequest();
        dto.setUsername("unknown");
        dto.setPassword("1234");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));
    }

    @Test
    void login_ShouldThrow_WhenPasswordInvalid() {
        UserLoginDTORequest dto = new UserLoginDTORequest();
        dto.setUsername("user");
        dto.setPassword("wrong");

        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));
    }

    @Test
    void login_ShouldReturnUserDTO_WhenSuccess() {
        UserLoginDTORequest dto = new UserLoginDTORequest();
        dto.setUsername("user");
        dto.setPassword("pass");

        User user = new User();
        user.setPassword("encodedPass");

        UserDTOResponse dtoResponse = new UserDTOResponse();
        dtoResponse.setUsername("user");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(userMapper.toUserDTOResponse(user)).thenReturn(dtoResponse);

        UserDTOResponse result = authService.login(dto);
        assertEquals("user", result.getUsername());
    }
}