package com.library.server.repositories;

import com.library.server.models.Entities.Book;
import com.library.server.models.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // For viewing only available books
    List<Book> findByAvailability(BookStatus availability);

    // For searching the catalog
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}