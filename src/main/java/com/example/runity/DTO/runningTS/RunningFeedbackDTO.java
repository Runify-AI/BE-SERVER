package com.example.runity.DTO.runningTS;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunningFeedbackDTO {
    private float distance;
    private int duration;
    private float avgPace;
    private int stopCount;
    private FeedbackSummaryDTO feedbackSummary;
    private int focusScore;
}

