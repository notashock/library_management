package com.library.server.services;

import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.dto.IssueResponseDTO;
import com.library.server.dto.MemberResponseDTO;
import com.library.server.models.Entities.Book;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.IssueStatus;
import com.library.server.models.enums.Role;
import com.library.server.repositories.IssueRepository;
import com.library.server.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private MemberService memberService;

    private Member mockMember;
    private IssueRecord mockIssue;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .memberId(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .role(Role.MEMBER)
                .build();

        Book mockBook = Book.builder().bookId(100L).title("1984").author("George Orwell").build();

        mockIssue = IssueRecord.builder()
                .issueId(10L)
                .member(mockMember)
                .book(mockBook)
                .issueDate(LocalDate.now())
                .status(IssueStatus.ACTIVE)
                .build();
    }

    @Test
    void getMemberDetails_Success_ReturnsDTO() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));

        MemberResponseDTO result = memberService.getMemberDetails(1L);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberIssues_Success_ReturnsDTOList() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(issueRepository.findByMember_MemberId(1L)).thenReturn(List.of(mockIssue));

        List<IssueResponseDTO> result = memberService.getMemberIssues(1L);

        assertEquals(1, result.size());
        assertEquals("1984", result.get(0).getBook().getTitle());
        verify(issueRepository, times(1)).findByMember_MemberId(1L);
    }

    @Test
    void getMemberIssues_ThrowsException_WhenListIsEmpty() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(issueRepository.findByMember_MemberId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberIssues(1L));

        assertEquals("No issued books found for member ID: 1", exception.getMessage());
    }
}