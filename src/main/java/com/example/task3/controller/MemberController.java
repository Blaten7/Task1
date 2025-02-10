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
        Member registeredMember = authService.register(member.getEmail(), member.getPassword(), member.getName());
        return ResponseEntity.ok(registeredMember);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member member) {
        String token = authService.login(member.getEmail(), member.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
