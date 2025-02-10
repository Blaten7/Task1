package com.example.task3.controller;

import com.example.task3.entity.Feedback;
import com.example.task3.entity.Member;
import com.example.task3.repository.MemberRepository;
import com.example.task3.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final MemberRepository memberRepository;

    public FeedbackController(FeedbackService feedbackService, MemberRepository memberRepository) {
        this.feedbackService = feedbackService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Feedback> createFeedback(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody Map<String, Object> request) {
        Long chatId = Long.valueOf(request.get("chatId").toString());
        boolean isPositive = Boolean.parseBoolean(request.get("isPositive").toString());
        Optional<Member> member = memberRepository.findByEmail(userDetails.getUsername());
        Feedback feedback = feedbackService.createFeedback(member.get(), chatId, isPositive);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping
    public ResponseEntity<Page<Feedback>> getFeedbacks(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestParam(required = false) Boolean isPositive,
                                                       Pageable pageable) {
        Optional<Member> member = memberRepository.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(feedbackService.getFeedbacks(member.get(), isPositive, pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{feedbackId}/status")
    public ResponseEntity<Feedback> updateFeedbackStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Long feedbackId,
                                                         @RequestBody Map<String, String> request) {
        System.out.println("관리자전용, 피드백 상태 수정 컨트롤러 진입");
        String status = request.get("status");
        Optional<Member> member = memberRepository.findByEmail(userDetails.getUsername());
        Feedback updatedFeedback = feedbackService.updateFeedbackStatus(member.get(), feedbackId, status);
        return ResponseEntity.ok(updatedFeedback);
    }
}
