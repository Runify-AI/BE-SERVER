package com.example.runity.DTO;

import lombok.Data;

import java.sql.Time;

@Data
public class RouteRequestDTO {
    private Long userId;
    private String startPoint;
    private String endPoint;
    private Time estimatedTime;
    private Float distance;
    private RouteChoiceRequestDTO routeChoiceRequestDTO;
}
