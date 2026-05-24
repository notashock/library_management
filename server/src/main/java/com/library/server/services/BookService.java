package com.library.server.services;

import com.library.server.dto.BookRequestDTO;
import com.library.server.dto.BookResponseDTO;
import com.library.server.models.Entities.Book;
import com.library.server.models.enums.BookStatus;
import com.library.server.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookResponseDTO addBook(BookRequestDTO request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .availability(BookStatus.AVAILABLE)
                .build();

        Book savedBook = bookRepository.save(book);
        return mapToResponseDTO(savedBook);
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // FIXED: Now returns DTOs
    public List<BookResponseDTO> getAvailableBooks() {
        return bookRepository.findByAvailability(BookStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // FIXED: Now returns DTOs
    public List<BookResponseDTO> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private BookResponseDTO mapToResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .availability(book.getAvailability())
                .build();
    }
}