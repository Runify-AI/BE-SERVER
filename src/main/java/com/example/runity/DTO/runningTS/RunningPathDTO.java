package com.example.runity.DTO.runningTS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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

    @Schema(description = "GPS 좌표 (위도,경도)", example = "37.5665,126.9780")
    @Pattern(
            regexp = "^[-+]?\\d{1,3}\\.\\d+\\s*,\\s*[-+]?\\d{1,3}\\.\\d+$",
            message = "좌표는 '위도,경도' 형식이어야 합니다. 예: 37.5665,126.9780"
    )
    private String coordinate;

    private LocalTime elapsedTime;
    private Float typeEta;
    private Float typePace;
    private Float typeStop;
}
