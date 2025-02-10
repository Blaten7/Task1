package com.example.task3.dto;

import com.example.task3.entity.Chat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseDto {
    private Long id;
    private String question;
    private String answer;
    private Long threadId;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.question = chat.getQuestion();
        this.answer = chat.getAnswer();
        this.threadId = chat.getThread().getId();
    }
}
