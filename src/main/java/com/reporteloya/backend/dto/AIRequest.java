package com.reporteloya.backend.dto;

import java.util.UUID;

public class AIRequest {
    private String user_id;
    private String name;
    private String role;

    public AIRequest(UUID user_id, String name, String role) {
        this.user_id = user_id.toString();
        this.name = name;
        this.role = role;
    }

    public String getUser_id() { return user_id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}