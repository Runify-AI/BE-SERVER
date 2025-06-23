package com.example.runity.DTO.runningTS;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class Statics {
    private float distance;
    private float averagePace;
    private int duration;
    private int stopCount;
    private FeedbackSummary feedback_summary;
    private int focus_score;
}