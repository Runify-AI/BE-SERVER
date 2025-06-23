package com.example.runity.DTO.runningTS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private CoordinateDTO coordinate;

    private LocalTime elapsedTime;
    private Float typeEta;
    private Float typePace;
    private Float typeStop;


    @Data
    @AllArgsConstructor
    public static class CoordinateDTO {
        private double latitude;
        private double longitude;

        public CoordinateDTO() {}

        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }

        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        }
}
