package com.reporteloya.backend.config;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitService {

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 5 * 60 * 1000L; // 5 minutos

    private record Window(int count, long resetAt) {}

    private final ConcurrentHashMap<String, Window> store = new ConcurrentHashMap<>();

    public boolean isAllowed(String ip) {
        long now = Instant.now().toEpochMilli();

        store.compute(ip, (key, win) -> {
            if (win == null || now >= win.resetAt()) {
                return new Window(1, now + WINDOW_MS);
            }
            return new Window(win.count() + 1, win.resetAt());
        });

        Window current = store.get(ip);
        return current.count() <= MAX_REQUESTS;
    }
}
