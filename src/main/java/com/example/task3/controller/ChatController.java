package com.example.task3.controller;

import com.example.task3.entity.Chat;
import com.example.task3.entity.Member;
import com.example.task3.repository.MemberRepository;
import com.example.task3.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    public ChatController(ChatService chatService, MemberRepository memberRepository) {
        this.chatService = chatService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Map<String, String> request) {
        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String question = request.get("question");
        String answer = request.get("answer");

        Chat chat = chatService.createChat(member, question, answer);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<List<Chat>> getChatsByThread(@PathVariable Long threadId) {
        return ResponseEntity.ok(chatService.getChatsByThread(threadId));
    }

    @DeleteMapping("/thread/{threadId}")
    public ResponseEntity<Void> deleteThread(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long threadId) {
        Optional<Member> member = memberRepository.findByEmail(userDetails.getUsername());
        if (member.isPresent()) {
            chatService.deleteThread(member.get(), threadId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}