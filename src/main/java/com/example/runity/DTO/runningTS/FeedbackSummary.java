package com.example.runity.DTO.runningTS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@Getter
public class FeedbackSummary {
    private String main;
    private String advice;
    private double early_speed_deviation;
}
