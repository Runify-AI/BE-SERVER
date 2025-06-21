package com.example.runity.DTO;

import lombok.Data;
import java.util.List;
import java.sql.Time;

@Data
public class RouteRequestDTO {
    private String startPoint;
    private String endPoint;
    private Time estimatedTime;
    private Float distance;
    private RouteChoiceRequestDTO routeChoiceRequestDTO;
    private List<CoordinateDTO> coordinates;

    @Data
    public static class CoordinateDTO {
        private double latitude;
        private double longitude;
    }
}
