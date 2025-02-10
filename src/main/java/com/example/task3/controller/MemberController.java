package com.example.task3.controller;

import com.example.task3.entity.Member;
import com.example.task3.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member) {
        System.out.println("입력한 비밀번호"+member.getPassword());
        Member registeredMember = authService.register(member.getEmail(), member.getPassword(), member.getName());
        return ResponseEntity.ok(registeredMember);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member member) {
        System.out.println("로그인 컨트롤러 진입");
        System.out.println("이메일 : " + member.getEmail());
        System.out.println("비번 : " + member.getPassword());
        String token = authService.login(member.getEmail(), member.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
