package com.library.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.server.dto.BookRequestDTO;
import com.library.server.dto.BookResponseDTO;
import com.library.server.models.enums.BookStatus;
import com.library.server.security.CustomUserDetailsService;
import com.library.server.security.JwtAuthenticationFilter;
import com.library.server.security.JwtService;
import com.library.server.security.SecurityConfig;
import com.library.server.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BookController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
// --- THE FIX FOR SECURITY TESTING: Import your actual security rules ---
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class RoleBasedAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    // We must mock these so the SecurityConfig and Filter can boot up properly
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = BookRequestDTO.builder()
                .title("Advanced Security")
                .author("Spring Guru")
                .build();

        responseDTO = BookResponseDTO.builder()
                .bookId(1L)
                .title("Advanced Security")
                .availability(BookStatus.AVAILABLE)
                .build();
    }

    // ==========================================
    // TEST 1: UNAUTHENTICATED ACCESS (Anonymous)
    // ==========================================
    @Test
    @WithAnonymousUser
    void unauthenticatedUser_CannotAccessProtectedEndpoints() throws Exception {
        // Expect 401 Unauthorized (because they aren't logged in)
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    // ==========================================
    // TEST 2: MEMBER ROLE RESTRICTIONS
    // ==========================================
    @Test
    @WithMockUser(roles = "MEMBER")
    void member_CanViewBooks_ButCannotAddBooks() throws Exception {

        // 1. Members CAN view the catalog (GET is allowed)
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());

        // 2. Members CANNOT add a book (POST is strictly LIBRARIAN)
        // Expect 403 Forbidden (they are logged in, but lack the correct role)
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    // ==========================================
    // TEST 3: LIBRARIAN ROLE PRIVILEGES
    // ==========================================
    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void librarian_CanAddBooks() throws Exception {

        // Arrange our mock service
        when(bookService.addBook(any(BookRequestDTO.class))).thenReturn(responseDTO);

        // Librarians CAN add a book
        // Expect 201 Created
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }
}