package com.library.server.repositories;

import com.library.server.models.Entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // This is the specific method called by your SecurityConfig
    Optional<Member> findByEmail(String email);

}