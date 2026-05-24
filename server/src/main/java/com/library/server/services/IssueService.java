package com.library.server.services;

import com.library.server.models.Entities.Book;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.models.enums.BookStatus;
import com.library.server.models.enums.IssueStatus;
import com.library.server.repositories.BookRepository;
import com.library.server.repositories.IssueRepository;
import com.library.server.repositories.MemberRepository;
import com.library.server.Exceptions.BookNotAvailableException;
import com.library.server.Exceptions.LimitExceededException;
import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.Exceptions.InvalidReturnException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IssueRecord issueBook(Long bookId, Long memberId) {
        // 1. Verify Member exists
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        // 2. Business Rule: Maximum 3 active issues
        long activeIssues = issueRepository.countByMember_MemberIdAndStatus(memberId, IssueStatus.ACTIVE);
        if (activeIssues >= 3) {
            throw new LimitExceededException("Member has reached the maximum limit of 3 books");
        }

        // 3. Business Rule: Book must be available
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailability() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Book is already issued or unavailable");
        }

        // 4. Perform issue operation
        book.setAvailability(BookStatus.ISSUED); // Mark book as issued [cite: 45]
        bookRepository.save(book);

        IssueRecord issueRecord = IssueRecord.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .status(IssueStatus.ACTIVE)
                .build();

        return issueRepository.save(issueRecord);
    }

    @Transactional
    public IssueRecord returnBook(Long issueId) {
        // 1. Locate existing issue record
        IssueRecord issueRecord = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found"));

        if (issueRecord.getStatus() == IssueStatus.RETURNED) {
            throw new InvalidReturnException("Book has already been returned");
        }

        // 2. Update issue details [cite: 30]
        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setStatus(IssueStatus.RETURNED);

        // 3. Mark book as available [cite: 31]
        Book book = issueRecord.getBook();
        book.setAvailability(BookStatus.AVAILABLE);

        bookRepository.save(book);
        return issueRepository.save(issueRecord);
    }
}