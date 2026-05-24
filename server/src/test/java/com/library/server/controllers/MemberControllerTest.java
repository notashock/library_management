package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.dto.IssueResponseDTO;
import com.library.server.dto.MemberResponseDTO;
import com.library.server.models.enums.IssueStatus;
import com.library.server.models.enums.Role;
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

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private MemberResponseDTO mockMemberDTO;
    private IssueResponseDTO mockIssueDTO;

    @BeforeEach
    void setUp() {
        // Mock the new DTOs instead of Entities
        mockMemberDTO = MemberResponseDTO.builder()
                .memberId(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(Role.MEMBER)
                .build();

        mockIssueDTO = IssueResponseDTO.builder()
                .issueId(10L)
                .issueDate(LocalDate.now())
                .status(IssueStatus.ACTIVE)
                .book(IssueResponseDTO.BookSummary.builder()
                        .bookId(1L)
                        .title("The Hobbit")
                        .author("J.R.R. Tolkien")
                        .build())
                .build();
    }

    @Test
    void getMemberDetails_Returns200Ok_WithApiResponse() throws Exception {
        when(memberService.getMemberDetails(1L)).thenReturn(mockMemberDTO);

        mockMvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.memberId").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void getMemberDetails_ThrowsResourceNotFound_Returns404() throws Exception {
        when(memberService.getMemberDetails(99L)).thenThrow(new ResourceNotFoundException("Member not found"));

        mockMvc.perform(get("/members/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Member not found"));
    }

    @Test
    void getMemberIssues_Returns200Ok_WithApiResponse() throws Exception {
        when(memberService.getMemberIssues(1L)).thenReturn(List.of(mockIssueDTO));

        mockMvc.perform(get("/members/1/issues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].issueId").value(10))
                .andExpect(jsonPath("$.data[0].book.title").value("The Hobbit"));
    }
}