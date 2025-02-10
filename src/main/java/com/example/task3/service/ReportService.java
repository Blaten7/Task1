package com.example.task3.service;

import com.example.task3.entity.Chat;
import com.example.task3.entity.Member;
import com.example.task3.repository.ChatRepository;
import com.example.task3.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public ReportService(ChatRepository chatRepository, MemberRepository memberRepository) {
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
    }

    public String generateReport() throws IOException {
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        long newMembers = memberRepository.countNewMembersSince(oneDayAgo);
        long chatCount = chatRepository.countChatsSince(oneDayAgo);

        String filePath = "report.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("New Members,Chats Created\n");
            writer.append(newMembers + "," + chatCount + "\n");
        }
        return filePath;
    }

    public long getNewMembersCount() {
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        return memberRepository.countNewMembersSince(oneDayAgo);
    }

    public long getChatCount() {
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        return chatRepository.countChatsSince(oneDayAgo);
    }
}
