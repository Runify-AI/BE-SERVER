package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@Setter
public class RunningSessionSummaryDTO {
    private Float distanceDiff;                       // 거리 비교값
    private Float totalDistance;
    private Integer runCount;
    private Float avgSpeed;
    private LocalTime totalRunTime;
    private List<RunningSessionDTO> runningSessionDTO;
}
