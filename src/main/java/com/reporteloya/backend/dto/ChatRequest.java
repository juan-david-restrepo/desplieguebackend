package com.reporteloya.backend.dto;

import java.util.UUID;

public class ChatRequest {
    private String user_id;
    private Integer conversation_id;
    private String message;

    public ChatRequest(UUID user_id, Integer conversation_id, String message) {
        this.user_id = user_id.toString();
        this.conversation_id = conversation_id;
        this.message = message;
    }

    public String getUser_id() { return user_id; }
    public Integer getConversation_id() { return conversation_id; }
    public String getMessage() { return message; }
}