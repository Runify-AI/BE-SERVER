package com.example.runity.DTO.route;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class RouteRequestDTO {
    private String startPoint;
    private String endPoint;
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime estimatedTime;
    private Float distance;
    private RouteChoiceRequestDTO routeChoiceRequestDTO;
    private Long routineId;
}
