package com.library.server.security;

import com.library.server.models.Entities.Member;
import com.library.server.models.enums.Role;
import com.library.server.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        // Set up a valid member entity to act as our UserDetails object
        mockMember = Member.builder()
                .memberId(1L)
                .email("librarian@library.com")
                .name("Alice")
                .password("encoded_password_here")
                .role(Role.LIBRARIAN)
                .build();
    }

    @Test
    void loadUserByUsername_Success_ReturnsUserDetails() {
        // Arrange
        String email = "librarian@library.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(mockMember));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("encoded_password_here", result.getPassword());

        // Verify the repository was queried exactly once
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ThrowsUsernameNotFoundException() {
        // Arrange
        String email = "unknown@library.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email));

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(memberRepository, times(1)).findByEmail(email);
    }
}