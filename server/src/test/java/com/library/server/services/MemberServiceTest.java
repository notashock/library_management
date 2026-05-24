package com.library.server.services;

import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.models.Entities.Member;
import com.library.server.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .memberId(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void getMemberDetails_Success() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));

        // Act
        Member result = memberService.getMemberDetails(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberDetails_ThrowsResourceNotFoundException() {
        // Arrange
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberDetails(99L));

        assertEquals("Member not found with ID: 99", exception.getMessage());
        verify(memberRepository, times(1)).findById(99L);
    }
}