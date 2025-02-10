package com.example.task3.service;

import com.example.task3.entity.Feedback;
import com.example.task3.entity.Member;
import com.example.task3.entity.Chat;
import com.example.task3.repository.FeedbackRepository;
import com.example.task3.repository.ChatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ChatRepository chatRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, ChatRepository chatRepository) {
        this.feedbackRepository = feedbackRepository;
        this.chatRepository = chatRepository;
    }

    public Feedback createFeedback(Member member, Long chatId, boolean isPositive) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        if (!chat.getThread().getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("You can only provide feedback for your own chats");
        }

        Feedback feedback = new Feedback();
        feedback.setMember(member);
        feedback.setChat(chat);
        feedback.setPositive(isPositive);
        feedback.setStatus("pending");
        return feedbackRepository.save(feedback);
    }

    public Page<Feedback> getFeedbacks(Member member, Boolean isPositive, Pageable pageable) {
        if (member.getRole().equals("ADMIN")) {
            return isPositive != null ?
                    feedbackRepository.findByIsPositive(isPositive, pageable) :
                    feedbackRepository.findAll(pageable);
        }
        return isPositive != null ?
                feedbackRepository.findByMemberAndIsPositive(member, isPositive, pageable) :
                feedbackRepository.findByMember(member, pageable);
    }

    public Feedback updateFeedbackStatus(Member member, Long feedbackId, String status) {
        if (!member.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("Only admins can update feedback status");
        }
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }
}
