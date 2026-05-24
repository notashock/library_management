package com.library.server.services;

import com.library.server.dto.AuthRequestDTO;
import com.library.server.dto.AuthResponseDTO;
import com.library.server.dto.RegisterRequestDTO;
import com.library.server.models.Entities.Member;
import com.library.server.repositories.MemberRepository;
import com.library.server.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        var member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        memberRepository.save(member);
        var jwtToken = jwtService.generateToken(member);
        return AuthResponseDTO.builder().email(member.getEmail()).role(member.getRole()).token(jwtToken).build();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(member);
        return AuthResponseDTO.builder().email(member.getEmail()).role(member.getRole()).token(jwtToken).build();
    }
}