package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.dto.AuthRequestDTO;
import com.library.server.dto.AuthResponseDTO;
import com.library.server.dto.RegisterRequestDTO;
import com.library.server.models.enums.Role;
import com.library.server.security.CustomUserDetailsService;
import com.library.server.security.JwtService;
import com.library.server.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // Security Mocks
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private RegisterRequestDTO registerRequest;
    private AuthRequestDTO authRequest;
    private AuthResponseDTO authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequestDTO.builder()
                .name("New User")
                .email("user@library.com")
                .password("password123")
                .role(Role.MEMBER)
                .build();

        authRequest = AuthRequestDTO.builder()
                .email("user@library.com")
                .password("password123")
                .build();

        authResponse = AuthResponseDTO.builder()
                .token("mock-jwt-token-string")
                .build();
    }

    @Test
    void register_Returns200Ok_WithToken() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token-string"));
    }

    @Test
    void authenticate_Returns200Ok_WithToken() throws Exception {
        // Arrange
        when(authService.authenticate(any(AuthRequestDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token-string"));
    }
}