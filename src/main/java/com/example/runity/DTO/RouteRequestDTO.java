package com.example.runity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Time;

@Data
public class RouteRequestDTO {
    private String startPoint;
    private String endPoint;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime estimatedTime;
    private Float distance;
    private RouteChoiceRequestDTO routeChoiceRequestDTO;
    private List<CoordinateDTO> coordinates;

    @Data
    public static class CoordinateDTO {
        private double latitude;
        private double longitude;
    }
}
