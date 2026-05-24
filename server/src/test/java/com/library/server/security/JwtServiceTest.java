package com.library.server.security;

import com.library.server.models.Entities.Member;
import com.library.server.models.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private Member mockMember;
    private String generatedToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Injecting the variables directly into the compiled class
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L); // 24 hours

        mockMember = Member.builder()
                .memberId(1L)
                .email("test@library.com")
                .name("Test User")
                .password("password123")
                .role(Role.MEMBER)
                .build();

        generatedToken = jwtService.generateToken(mockMember);
    }

    @Test
    void generateToken_ShouldReturnValidString() {
        assertNotNull(generatedToken);
        assertFalse(generatedToken.isEmpty());
        // JWTs always have 3 parts separated by dots (header.payload.signature)
        assertEquals(3, generatedToken.split("\\.").length);
    }

    @Test
    void extractUsername_ShouldReturnEmailFromToken() {
        String extractedEmail = jwtService.extractUsername(generatedToken);

        assertNotNull(extractedEmail);
        assertEquals("test@library.com", extractedEmail);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForCorrectUser() {
        boolean isValid = jwtService.isTokenValid(generatedToken, mockMember);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForIncorrectUser() {
        // Create a different user
        Member wrongMember = Member.builder()
                .email("hacker@library.com")
                .build();

        boolean isValid = jwtService.isTokenValid(generatedToken, wrongMember);

        assertFalse(isValid);
    }

    @Test
    void extractUsername_ShouldThrowSignatureException_WhenTokenIsTampered() {
        // Tamper with the token by changing the last character of the signature
        String tamperedToken = generatedToken.substring(0, generatedToken.length() - 1) + "X";

        // Attempting to extract data from a tampered token should throw a security exception
        assertThrows(SignatureException.class, () -> jwtService.extractUsername(tamperedToken));
    }

    @Test
    void isTokenExpired_ShouldThrowException_WhenExpirationIsZero() {
        // Create a temporary JwtService where tokens expire immediately (0 milliseconds)
        JwtService expiredJwtService = new JwtService();
        ReflectionTestUtils.setField(expiredJwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(expiredJwtService, "jwtExpiration", 0L);

        String instantlyExpiredToken = expiredJwtService.generateToken(mockMember);

        // Attempting to read an expired token should throw ExpiredJwtException
        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(instantlyExpiredToken));
    }
}