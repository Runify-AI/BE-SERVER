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
    private Float totalDistance;      // 총 거리
    private LocalTime totalRunTime;   // 총 러닝 시간
    private LocalDate date;
    private Integer runCount;         // 러닝 횟수
    private Float avgSpeed;           // 평균 속도
    private List<RealTimeRunningDTO> runningSessions;  // 러닝 기록 리스트

    // 날씨
    private String weather;    // 예: "Cloudy"
    private Double temperature; // 예: 23.5도
    private Double humidity;   // 예: 70%
}