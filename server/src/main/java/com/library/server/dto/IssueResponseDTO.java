package com.library.server.dto;

import com.library.server.models.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDTO {

    private Long issueId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private IssueStatus status;

    // We use safe, nested summary objects instead of the raw database Entities
    private BookSummary book;
    private MemberSummary member;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookSummary {
        private Long bookId;
        private String title;
        private String author;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSummary {
        private Long memberId;
        private String name;
        private String email;
    }
}