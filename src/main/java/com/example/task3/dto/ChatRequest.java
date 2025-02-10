package com.example.task3.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String question;
    private Boolean isStreaming;
    private String model;
}
