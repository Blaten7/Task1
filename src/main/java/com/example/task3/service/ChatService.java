package com.example.task3.service;

import com.example.task3.entity.Chat;
import com.example.task3.entity.ChatThread;
import com.example.task3.entity.Member;
import com.example.task3.entity.ChatThread;
import com.example.task3.repository.ChatThreadRepository;
import com.example.task3.repository.ChatThreadRepository;
import com.example.task3.repository.ChatRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class ChatService {
    private final ChatThreadRepository chatThreadRepository;
    private final ChatRepository chatRepository;

    public ChatService(ChatThreadRepository chatThreadRepository, ChatRepository chatRepository) {
        this.chatThreadRepository = chatThreadRepository;
        this.chatRepository = chatRepository;
    }

    public Chat createChat(Member member, String question, String answer) {
        ChatThread thread = chatThreadRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
                .filter(t -> t.getCreatedAt() != null && Instant.now().minusSeconds(1800).isBefore(t.getCreatedAt()))
                .orElseGet(() -> {
                    ChatThread newThread = new ChatThread();
                    newThread.setMember(member);
                    return chatThreadRepository.save(newThread);
                });

        Chat chat = new Chat();
        chat.setThread(thread);
        chat.setQuestion(question);
        chat.setAnswer(answer);
        return chatRepository.save(chat);
    }

    public List<Chat> getChatsByThread(Long threadId) {
        return chatRepository.findByThreadIdOrderByCreatedAtAsc(threadId);
    }
}
