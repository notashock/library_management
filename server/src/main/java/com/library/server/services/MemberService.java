package com.library.server.services;

import com.library.server.models.Entities.Member;
import com.library.server.repositories.MemberRepository;
import com.library.server.Exceptions.ResourceNotFoundException; // Add this import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getMemberDetails(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId)); // Updated!
    }
}