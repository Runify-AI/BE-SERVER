package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RunningPathDTO {
    private LocalDateTime timestamp;     // 측정 시각
    private Double pace;                 // 페이스 (/km)
    private Double distance;             // 누적 거리 (km)
    private Double speed;                // 속도 (km/h)
    private String coordinate;       // GPS 좌표 (별도 클래스)
}
