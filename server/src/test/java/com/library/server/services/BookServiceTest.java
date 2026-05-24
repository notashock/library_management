package com.library.server.services;

import com.library.server.dto.BookRequestDTO;
import com.library.server.dto.BookResponseDTO;
import com.library.server.models.Entities.Book;
import com.library.server.models.enums.BookStatus;
import com.library.server.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book mockBook;
    private BookRequestDTO mockRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup the Entity that the repository will "return"
        mockBook = Book.builder()
                .bookId(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .availability(BookStatus.AVAILABLE)
                .build();

        // Setup the incoming DTO from the controller
        mockRequestDTO = BookRequestDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .build();
    }

    @Test
    void addBook_Success() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        // Act
        BookResponseDTO result = bookService.addBook(mockRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthor());
        assertEquals(BookStatus.AVAILABLE, result.getAvailability()); // Ensures default status is set

        // Verify the repository was called exactly once to save
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getAllBooks_Success() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(mockBook));

        // Act
        List<BookResponseDTO> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAvailableBooks_Success() {
        // Arrange
        when(bookRepository.findByAvailability(BookStatus.AVAILABLE)).thenReturn(List.of(mockBook));

        // Act
        List<BookResponseDTO> result = bookService.getAvailableBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookStatus.AVAILABLE, result.get(0).getAvailability());
        verify(bookRepository, times(1)).findByAvailability(BookStatus.AVAILABLE);
    }

    @Test
    void searchBooks_Success() {
        // Arrange
        String query = "Clean";
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query))
                .thenReturn(List.of(mockBook));

        // Act
        List<BookResponseDTO> result = bookService.searchBooks(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }
}