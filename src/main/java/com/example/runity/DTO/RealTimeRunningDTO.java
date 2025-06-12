package com.example.runity.DTO;

import com.example.runity.domain.RunningPathTS;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class RealTimeRunningDTO {
    private LocalDateTime endTime;
    private boolean isCompleted;
    private String giveUpReason;
    private int effortLevel;
    private float avgPace;
    private float avgSpeed;
    private List<RunningPathTS> pathList;
}