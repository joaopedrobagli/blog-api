package com.joaobagli.blog_api.service;

import com.joaobagli.blog_api.config.JwtService;
import com.joaobagli.blog_api.dto.auth.AuthResponse;
import com.joaobagli.blog_api.dto.auth.LoginRequest;
import com.joaobagli.blog_api.dto.auth.RegisterRequest;
import com.joaobagli.blog_api.entity.Role;
import com.joaobagli.blog_api.entity.User;
import com.joaobagli.blog_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("joaobagli");
        registerRequest.setEmail("joao@email.com");
        registerRequest.setPassword("123456");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("joao@email.com");
        loginRequest.setPassword("123456");

        user = User.builder()
                .id(1L)
                .username("joaobagli")
                .email("joao@email.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_deveRetornarTokenQuandoDadosValidos() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("fake_token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("fake_token", response.getToken());
        assertEquals("joao@email.com", response.getUsername());
        assertEquals("USER", response.getRole());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_deveLancarExcecaoQuandoEmailJaExiste() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_deveRetornarTokenQuandoCredenciaisValidas() {
        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("joao@email.com", "123456"));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("fake_token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("fake_token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void login_deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("joao@email.com", "123456"));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}