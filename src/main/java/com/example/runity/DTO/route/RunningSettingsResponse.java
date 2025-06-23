package com.example.runity.DTO.route;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class RunningSettingsResponse {
    private List<RouteCoordinateDTO> routePoints;
    private String duration;
    private String startTime;
    private String estimatedEndTime;
    private String startPoint;
    private String endPoint;
    private String targetPace;
}
