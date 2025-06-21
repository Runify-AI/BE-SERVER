package com.example.runity.domain;

import lombok.Builder;

import java.time.Instant;

@Builder
public class FeedbackPoint {
    private Instant timestamp;
    private String type;
    private String semiType;
    private String message;
    private Long userId;
    private Long sessionId;
}
