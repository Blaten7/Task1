package com.example.task3.service;

import com.example.task3.entity.Chat;
import com.example.task3.entity.Member;
import com.example.task3.repository.ChatRepository;
import com.example.task3.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

@Service
public class ReportService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public ReportService(ChatRepository chatRepository, MemberRepository memberRepository) {
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
    }

    public String generateReport() throws IOException {
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        List<Chat> dailyChats = chatRepository.findAllChatsSince(oneDayAgo);

        String filePath = "daily_report.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write('\ufeff');

            writer.write("번호\t생성 시간\t사용자 이메일\t사용자 이름\t대화 내용\n");

            for (Chat chat : dailyChats) {
                Member creator = chat.getMember();
                String email = creator != null ? creator.getEmail() : "(삭제됨)";
                String name = creator != null ? creator.getName() : "(삭제됨)";

                writer.write(String.format("%d\t%s\t%s\t%s\t%s\n",
                        chat.getId(),
                        formatter.format(chat.getCreatedAt()),
                        email,
                        name,
                        chat.getQuestion() != null ? chat.getQuestion() : ""
                ));
            }
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