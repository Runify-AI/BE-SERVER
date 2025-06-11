package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RunningPathDTO {
    private Long userId;
    private String timestamp;     // 측정 시각
    private Double pace;                 // 페이스 (/km)
    private Double distance;             // 누적 거리 (km)
    private Double speed;                // 속도 (km/h)
    private String coordinate;       // GPS 좌표
}
