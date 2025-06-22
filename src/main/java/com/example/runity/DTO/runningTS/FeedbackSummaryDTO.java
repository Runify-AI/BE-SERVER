package com.example.runity.DTO.runningTS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedbackSummaryDTO {
    private String main;
    private String advice;
    private float earlySpeedDeviation;
}
