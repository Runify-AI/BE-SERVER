package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponseDTO {
    private Long routeId;
    private String startPoint;
    private String endPoint;
    private String estimatedTime; // ex) "00:30:00"
    private Float distance;
    private String createdAt; // ex) "2025-06-09 00:00"
    private RouteChoiceResponseDTO routeChoiceResponseDTO;
}
