package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.dto.BookRequestDTO;
import com.library.server.dto.BookResponseDTO;
import com.library.server.models.enums.BookStatus;
import com.library.server.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass JWT security for pure controller testing
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = BookRequestDTO.builder()
                .title("Spring Boot in Action")
                .author("Craig Walls")
                .build();

        responseDTO = BookResponseDTO.builder()
                .bookId(1L)
                .title("Spring Boot in Action")
                .author("Craig Walls")
                .availability(BookStatus.AVAILABLE)
                .build();
    }

    @Test
    void addBook_Returns201Created_AndApiResponse() throws Exception {
        // Arrange
        when(bookService.addBook(any(BookRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Book added to catalog successfully"))
                .andExpect(jsonPath("$.data.title").value("Spring Boot in Action"))
                .andExpect(jsonPath("$.data.availability").value("AVAILABLE"));
    }

    @Test
    void getAllBooks_Returns200Ok_AndApiResponse() throws Exception {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(List.of(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.data[0].title").value("Spring Boot in Action"))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getAvailableBooks_Returns200Ok() throws Exception {
        // Arrange
        when(bookService.getAvailableBooks()).thenReturn(List.of(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/books/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].availability").value("AVAILABLE"));
    }
}