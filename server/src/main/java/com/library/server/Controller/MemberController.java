package com.library.server.controllers;

import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.Entities.Member;
import com.library.server.repositories.IssueRepository;
import com.library.server.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final IssueRepository issueRepository; // Injecting directly for the read-only lookup

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberDetails(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberDetails(memberId));
    }

    @GetMapping("/{memberId}/issues")
    public ResponseEntity<List<IssueRecord>> getMemberIssues(@PathVariable Long memberId) {
        // Uses the custom query we built in Phase 1
        return ResponseEntity.ok(issueRepository.findByMember_MemberId(memberId));
    }
}