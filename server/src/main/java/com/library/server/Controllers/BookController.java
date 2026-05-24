package com.library.server.Controllers;

import com.library.server.dto.ApiResponse;
import com.library.server.dto.BookRequestDTO;
import com.library.server.dto.BookResponseDTO;
import com.library.server.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> addBook(@RequestBody BookRequestDTO request) {
        BookResponseDTO savedBook = bookService.addBook(request);

        ApiResponse<BookResponseDTO> response = ApiResponse.<BookResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Book added to catalog successfully")
                .data(savedBook)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();

        ApiResponse<List<BookResponseDTO>> response = ApiResponse.<List<BookResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Books retrieved successfully")
                .data(books)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAvailableBooks() {
        List<BookResponseDTO> books = bookService.getAvailableBooks();

        ApiResponse<List<BookResponseDTO>> response = ApiResponse.<List<BookResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Available books retrieved successfully")
                .data(books)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // NEW: Exposing your search method
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> searchBooks(@RequestParam String query) {
        List<BookResponseDTO> books = bookService.searchBooks(query);

        ApiResponse<List<BookResponseDTO>> response = ApiResponse.<List<BookResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Search results retrieved successfully")
                .data(books)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}