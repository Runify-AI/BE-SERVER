package com.example.runity.DTO.route;

import com.example.runity.domain.Route;
import com.example.runity.domain.RouteChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponseDTO {
    private boolean completed;
    private Long routeId;
    private String startPoint;
    private String endPoint;
    private String estimatedTime;
    private Float distance;
    private String createdAt; // ex) "2025-06-09 00:00"
    private List<RouteChoiceResponseDTO> routeChoiceResponseDTO;
    private RoutineResponseDTO routineResponseDTO;

    public RouteResponseDTO(Route route, RoutineResponseDTO routineResponseDTO, List<RouteChoice> routeChoices) {
        this.completed = route.isCompleted();
        this.routeId = route.getRouteId();
        this.startPoint = route.getStartPoint();
        this.endPoint = route.getEndPoint();
        this.estimatedTime = route.getEstimatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.distance = route.getDistance();
        this.createdAt = route.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.routeChoiceResponseDTO = routeChoices.stream()
                .map(RouteChoiceResponseDTO::from)
                .collect(Collectors.toList());
        this.routineResponseDTO = routineResponseDTO;
    }

}
