package com.example.runity.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private Long routeId;
    private Long userId;
    private String startPoint;
    private String endPoint;
    private String estimatedTime;
    private Float distance;
    private LocalDateTime createAt;


    public static RouteResponse fromEntity(Route route) {
        return new RouteResponse(
                route.getRouteId(),
                route.getUserId(),
                route.getStartPoint(),
                route.getEndPoint(),
                route.getEstimatedTime().toString(),
                route.getDistance(),
                route.getCreateAt()
        );
    }
}
