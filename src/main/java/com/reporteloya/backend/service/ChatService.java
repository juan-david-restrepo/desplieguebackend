package com.reporteloya.backend.service;

import com.reporteloya.backend.dto.ChatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String aiChatUrl = "https://backend-ia-8in0.onrender.com/chat";

    public Map<String, Object> sendMessage(Long userId, Integer conversationId, String message) {

        ChatRequest request = new ChatRequest(userId, conversationId, message);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(aiChatUrl, request, Map.class);

        return response.getBody();
    }
}