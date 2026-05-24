package com.library.server.services;

import com.library.server.Exceptions.ResourceNotFoundException;
import com.library.server.dto.IssueResponseDTO;
import com.library.server.dto.MemberResponseDTO;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.repositories.IssueRepository;
import com.library.server.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    public MemberResponseDTO getMemberDetails(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));

        return MemberResponseDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }

    // UPDATED: Now returns the clean DTO instead of the database entity
    public List<IssueResponseDTO> getMemberIssues(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));

        List<IssueRecord> issueRecords = issueRepository.findByMember_MemberId(memberId);

        if (issueRecords.isEmpty()) {
            throw new ResourceNotFoundException("No issued books found for member ID: " + memberId);
        }

        // Map the entities to our clean DTOs
        return issueRecords.stream().map(issue -> IssueResponseDTO.builder()
                .issueId(issue.getIssueId())
                .issueDate(issue.getIssueDate())
                .returnDate(issue.getReturnDate())
                .status(issue.getStatus())
                .book(IssueResponseDTO.BookSummary.builder()
                        .bookId(issue.getBook().getBookId())
                        .title(issue.getBook().getTitle())
                        .author(issue.getBook().getAuthor())
                        .build())
                .member(IssueResponseDTO.MemberSummary.builder()
                        .memberId(issue.getMember().getMemberId())
                        .name(issue.getMember().getName())
                        .build())
                .build()
        ).collect(Collectors.toList());
    }
}