package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.models.Entities.Book;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.IssueStatus;
import com.library.server.models.enums.Role;
import com.library.server.repositories.IssueRepository;
import com.library.server.security.CustomUserDetailsService;
import com.library.server.security.JwtService;
import com.library.server.services.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = MemberController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    // We injected IssueRepository directly into the controller for the read-only list
    @MockBean
    private IssueRepository issueRepository;

    // Security Mocks
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Member mockMember;
    private IssueRecord mockIssueRecord;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .memberId(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(Role.MEMBER)
                .build();

        Book mockBook = Book.builder().bookId(1L).title("The Hobbit").build();

        mockIssueRecord = IssueRecord.builder()
                .issueId(10L)
                .book(mockBook)
                .member(mockMember)
                .issueDate(LocalDate.now())
                .status(IssueStatus.ACTIVE)
                .build();
    }

    @Test
    void getMemberDetails_Returns200Ok() throws Exception {
        // Arrange
        when(memberService.getMemberDetails(1L)).thenReturn(mockMember);

        // Act & Assert
        mockMvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getMemberDetails_ThrowsResourceNotFound_Returns404() throws Exception {
        // Arrange
        when(memberService.getMemberDetails(99L)).thenThrow(new ResourceNotFoundException("Member not found"));

        // Act & Assert
        mockMvc.perform(get("/members/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Member not found"));
    }

    @Test
    void getMemberIssues_Returns200Ok_WithList() throws Exception {
        // Arrange
        when(issueRepository.findByMember_MemberId(1L)).thenReturn(List.of(mockIssueRecord));

        // Act & Assert
        mockMvc.perform(get("/members/1/issues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].issueId").value(10))
                .andExpect(jsonPath("$[0].book.title").value("The Hobbit"));
    }
}