package com.library.server.repositories;

import com.library.server.models.Entities.IssueRecord;
import com.library.server.models.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<IssueRecord, Long> {

    // CRITICAL for Business Rule: Check if a member already has 3 active issues
    long countByMember_MemberIdAndStatus(Long memberId, IssueStatus status);

    // For viewing all books currently issued to a specific member
    List<IssueRecord> findByMember_MemberId(Long memberId);
}