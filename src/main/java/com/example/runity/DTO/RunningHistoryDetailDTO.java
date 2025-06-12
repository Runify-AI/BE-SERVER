package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class RunningHistoryDetailDTO {
    private Long recordId;
    private Long userId;
    private Float totalDistance;
    private LocalTime totalRunTime;
    private LocalDate date;
    private Integer runCount;
    private Float avgSpeed;
    private List<RealTimeRunningDTO> runningSessions;

    // 날씨
    private String weather;    // 예: "Cloudy"
    private Double temperature; // 예: 23.5도
    private Double humidity;   // 예: 70%
}