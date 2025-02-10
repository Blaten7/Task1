package com.example.task3.service;

import com.example.task3.entity.Chat;
import com.example.task3.entity.ChatThread;
import com.example.task3.entity.Member;
import com.example.task3.repository.ChatThreadRepository;
import com.example.task3.repository.ChatRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatThreadRepository chatThreadRepository;
    private final ChatRepository chatRepository;
    private final OpenAIService openAIService;

    public ChatService(ChatThreadRepository chatThreadRepository, ChatRepository chatRepository, OpenAIService openAIService) {
        this.chatThreadRepository = chatThreadRepository;
        this.chatRepository = chatRepository;
        this.openAIService = openAIService;
    }

    public Chat createChat(Member member, String question, String model) {
        ChatThread thread = chatThreadRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
                .filter(t -> t.getCreatedAt() != null && Instant.now().minusSeconds(1800).isBefore(t.getCreatedAt()))
                .orElseGet(() -> {
                    ChatThread newThread = new ChatThread();
                    newThread.setMember(member);
                    return chatThreadRepository.save(newThread);
                });
        String answers = openAIService.getAnswer(question, model);

        Chat chat = new Chat();
        chat.setThread(thread);
        chat.setQuestion(question);
        chat.setAnswer(answers);
        return chatRepository.save(chat);
    }


    public List<Chat> getChatsByThread(Long threadId) {
        return chatRepository.findByThreadIdOrderByCreatedAtAsc(threadId);
    }

    public void deleteThread(Member member, Long threadId) {
        ChatThread thread = chatThreadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Thread not found"));

        if (!thread.getMember().getId().equals(member.getId())) {
            throw new SecurityException("You are not authorized to delete this thread");
        }

        chatThreadRepository.delete(thread);
    }
}
