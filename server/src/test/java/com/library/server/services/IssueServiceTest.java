package com.library.server.services;

import com.library.server.Exceptions.BookNotAvailableException;
import com.library.server.Exceptions.InvalidReturnException;
import com.library.server.Exceptions.LimitExceededException;
import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.models.Entities.Book;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.BookStatus;
import com.library.server.models.enums.IssueStatus;
import com.library.server.repositories.BookRepository;
import com.library.server.repositories.IssueRepository;
import com.library.server.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private IssueService issueService;

    private Member mockMember;
    private Book mockBook;
    private IssueRecord mockIssueRecord;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder().memberId(1L).name("Test Member").build();
        mockBook = Book.builder().bookId(1L).title("Spring Boot Guide").availability(BookStatus.AVAILABLE).build();
        mockIssueRecord = IssueRecord.builder().issueId(1L).book(mockBook).member(mockMember).status(IssueStatus.ACTIVE).build();
    }

    // --- ISSUE BOOK TESTS ---

    @Test
    void issueBook_Success() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(issueRepository.countByMember_MemberIdAndStatus(1L, IssueStatus.ACTIVE)).thenReturn(2L); // Under limit
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(issueRepository.save(any(IssueRecord.class))).thenReturn(mockIssueRecord);

        // Act
        IssueRecord result = issueService.issueBook(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(BookStatus.ISSUED, mockBook.getAvailability());
        verify(bookRepository, times(1)).save(mockBook);
        verify(issueRepository, times(1)).save(any(IssueRecord.class));
    }

    @Test
    void issueBook_ThrowsLimitExceededException() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(issueRepository.countByMember_MemberIdAndStatus(1L, IssueStatus.ACTIVE)).thenReturn(3L); // At limit

        // Act & Assert
        Exception exception = assertThrows(LimitExceededException.class, () -> issueService.issueBook(1L, 1L));
        assertEquals("Member has reached the maximum limit of 3 books", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void issueBook_ThrowsBookNotAvailableException() {
        // Arrange
        mockBook.setAvailability(BookStatus.ISSUED); // Book is already taken
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(issueRepository.countByMember_MemberIdAndStatus(1L, IssueStatus.ACTIVE)).thenReturn(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        // Act & Assert
        Exception exception = assertThrows(BookNotAvailableException.class, () -> issueService.issueBook(1L, 1L));
        assertEquals("Book is already issued or unavailable", exception.getMessage());
    }

    @Test
    void issueBook_ThrowsResourceNotFoundException_ForMember() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> issueService.issueBook(1L, 1L));
    }

    // --- RETURN BOOK TESTS ---

    @Test
    void returnBook_Success() {
        // Arrange
        mockBook.setAvailability(BookStatus.ISSUED); // Assuming it was issued
        when(issueRepository.findById(1L)).thenReturn(Optional.of(mockIssueRecord));
        when(issueRepository.save(any(IssueRecord.class))).thenReturn(mockIssueRecord);

        // Act
        IssueRecord result = issueService.returnBook(1L);

        // Assert
        assertEquals(IssueStatus.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
        assertEquals(BookStatus.AVAILABLE, result.getBook().getAvailability()); // Book marked available
        verify(bookRepository, times(1)).save(result.getBook());
    }

    @Test
    void returnBook_ThrowsInvalidReturnException_WhenAlreadyReturned() {
        // Arrange
        mockIssueRecord.setStatus(IssueStatus.RETURNED); // Already returned
        when(issueRepository.findById(1L)).thenReturn(Optional.of(mockIssueRecord));

        // Act & Assert
        Exception exception = assertThrows(InvalidReturnException.class, () -> issueService.returnBook(1L));
        assertEquals("Book has already been returned", exception.getMessage());
    }
}