package com.example.task3.controller;

import com.example.task3.dto.ChatRequest;
import com.example.task3.dto.ChatResponseDto;
import com.example.task3.entity.Chat;
import com.example.task3.entity.Member;
import com.example.task3.repository.MemberRepository;
import com.example.task3.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
    public ResponseEntity<ChatResponseDto> createChat(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody ChatRequest request) {
        log.info("대화 생성 컨트롤러 진입");
        if (userDetails == null) {
            log.error("🚨 인증되지 않은 사용자 요청 발생");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("✅ 인증된 사용자: " + userDetails.getUsername());

        Member member = memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Chat chat = chatService.createChat(member, request.getQuestion(), request.getModel());
        return ResponseEntity.ok(new ChatResponseDto(chat));
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