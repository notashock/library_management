package com.library.server.dto;

import com.library.server.models.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private Long bookId;
    private String title;
    private String author;
    private BookStatus availability;
}