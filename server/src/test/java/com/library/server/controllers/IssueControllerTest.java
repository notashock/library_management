package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.Exceptions.LimitExceededException;
import com.library.server.dto.IssueRequestDTO;
import com.library.server.models.Entities.Book;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.IssueStatus;
import com.library.server.models.enums.Role;
import com.library.server.security.CustomUserDetailsService;
import com.library.server.security.JwtService;
import com.library.server.services.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// --- THE FIX: Exclude Spring's default security user manager ---
@WebMvcTest(
        controllers = IssueController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false) // Bypass JWT security for pure controller testing
class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    // --- THE FIX: Mock the Security Dependencies ---
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    // -----------------------------------------------

    private IssueRequestDTO requestDTO;
    private IssueRecord mockIssueRecord;

    @BeforeEach
    void setUp() {
        requestDTO = IssueRequestDTO.builder()
                .bookId(1L)
                .memberId(1L)
                .build();

        Book mockBook = Book.builder().bookId(1L).title("Java Guide").build();
        Member mockMember = Member.builder().memberId(1L).name("Test User").role(Role.MEMBER).build();

        mockIssueRecord = IssueRecord.builder()
                .issueId(100L)
                .book(mockBook)
                .member(mockMember)
                .issueDate(LocalDate.now())
                .status(IssueStatus.ACTIVE)
                .build();
    }

    @Test
    void issueBook_Returns201Created() throws Exception {
        // Arrange
        when(issueService.issueBook(anyLong(), anyLong())).thenReturn(mockIssueRecord);

        // Act & Assert
        mockMvc.perform(post("/issues/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.issueId").value(100))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.book.title").value("Java Guide"));
    }

    @Test
    void returnBook_Returns200Ok() throws Exception {
        // Arrange
        mockIssueRecord.setStatus(IssueStatus.RETURNED);
        mockIssueRecord.setReturnDate(LocalDate.now());
        when(issueService.returnBook(100L)).thenReturn(mockIssueRecord);

        // Act & Assert
        mockMvc.perform(put("/issues/return/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"))
                .andExpect(jsonPath("$.returnDate").exists());
    }

    @Test
    void issueBook_ThrowsLimitExceeded_Returns400BadRequest() throws Exception {
        // Arrange
        when(issueService.issueBook(anyLong(), anyLong()))
                .thenThrow(new LimitExceededException("Max 3 books allowed"));

        // Act & Assert
        mockMvc.perform(post("/issues/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Max 3 books allowed"));
    }
}