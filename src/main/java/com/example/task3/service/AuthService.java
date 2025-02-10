package com.example.task3.service;

import com.example.task3.entity.Member;
import com.example.task3.repository.MemberRepository;
import com.example.task3.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Member register(String email, String password, String name) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setName(name);
        member.setRole("member");
        return memberRepository.save(member);
    }

    public String login(String email, String password) {
        System.out.println("여 오냐");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        System.out.println("여 오냐2");
        Member member = memberRepository.findByEmail(email).orElseThrow();
        String token = jwtUtil.generateToken(member);
        System.out.println("토큰 : "+token);
        return token;

    }
}
