package com.library.server.controllers;

import com.library.server.dto.ApiResponse;
import com.library.server.dto.IssueResponseDTO;
import com.library.server.dto.MemberResponseDTO;
import com.library.server.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMemberDetails(@PathVariable Long memberId) {
        MemberResponseDTO member = memberService.getMemberDetails(memberId);

        ApiResponse<MemberResponseDTO> response = ApiResponse.<MemberResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Member details retrieved successfully")
                .data(member)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // UPDATED: Now returns ApiResponse<List<IssueResponseDTO>>
    @GetMapping("/{memberId}/issues")
    public ResponseEntity<ApiResponse<List<IssueResponseDTO>>> getMemberIssues(@PathVariable Long memberId) {
        List<IssueResponseDTO> issues = memberService.getMemberIssues(memberId);

        ApiResponse<List<IssueResponseDTO>> response = ApiResponse.<List<IssueResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Member issues retrieved successfully")
                .data(issues)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}