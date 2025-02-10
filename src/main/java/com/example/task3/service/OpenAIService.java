package com.example.task3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions"; // ✅ URL 수정

    @Value("${openai.api.key}")
    String OPENAI_API_KEY;

    public String getAnswer(String question, String model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        if (model.equals("") || model == null) {
            model = "gpt-3.5-turbo";
        }

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."), // 시스템 메시지 추가
                        Map.of("role", "user", "content", question)
                ),
                "max_tokens", 100
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseEntity.getBody().get("choices");
        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    }
}
