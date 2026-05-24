package com.library.server.services;

import com.library.server.dto.AuthRequestDTO;
import com.library.server.dto.AuthResponseDTO;
import com.library.server.dto.RegisterRequestDTO;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.Role;
import com.library.server.repositories.MemberRepository;
import com.library.server.security.JwtService;
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
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequest;
    private AuthRequestDTO authRequest;
    private Member mockMember;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequestDTO.builder()
                .name("New Librarian")
                .email("admin@library.com")
                .password("rawPassword")
                .role(Role.LIBRARIAN)
                .build();

        authRequest = AuthRequestDTO.builder()
                .email("admin@library.com")
                .password("rawPassword")
                .build();

        mockMember = Member.builder()
                .memberId(1L)
                .name("New Librarian")
                .email("admin@library.com")
                .password("encodedPassword")
                .role(Role.LIBRARIAN)
                .build();
    }

    @Test
    void register_Success() {
        // Arrange
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        when(jwtService.generateToken(any(Member.class))).thenReturn("mock-jwt-token");

        // Act
        AuthResponseDTO response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());

        // Verify that the password was actually encoded before saving
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(jwtService, times(1)).generateToken(any(Member.class));
    }

    @Test
    void authenticate_Success() {
        // Arrange
        // Note: AuthenticationManager returns an Authentication object, but we just need it to not throw an exception here
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(memberRepository.findByEmail("admin@library.com")).thenReturn(Optional.of(mockMember));
        when(jwtService.generateToken(mockMember)).thenReturn("mock-jwt-token");

        // Act
        AuthResponseDTO response = authService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());

        // Verify the auth manager was called to validate credentials
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(memberRepository, times(1)).findByEmail("admin@library.com");
    }
}