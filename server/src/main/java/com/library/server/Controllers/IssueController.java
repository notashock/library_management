package com.library.server.Controllers;

import com.library.server.dto.IssueRequestDTO;
import com.library.server.models.Entities.IssueRecord;
import com.library.server.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/issue")
    public ResponseEntity<IssueRecord> issueBook(@RequestBody IssueRequestDTO request) {
        IssueRecord record = issueService.issueBook(request.getBookId(), request.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/return/{issueId}")
    public ResponseEntity<IssueRecord> returnBook(@PathVariable Long issueId) {
        IssueRecord record = issueService.returnBook(issueId);
        return ResponseEntity.ok(record);
    }
}