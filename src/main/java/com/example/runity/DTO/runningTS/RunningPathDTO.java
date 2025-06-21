package com.example.runity.DTO.runningTS;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;

@Getter @Setter
public class RunningPathDTO {
    private Instant timestamp;              // 측정 시각, 밀리초 단위 epoch
    private Double pace;                 // 페이스 (/km)
    private Double distance;             // 누적 거리 (km)
    private Double speed;                // 속도 (km/h)
    private String coordinate;           // GPS 좌표
    private LocalTime elapsedTime;
    private Float typeEta;
    private Float typePace;
    private Float typeStop;
}
