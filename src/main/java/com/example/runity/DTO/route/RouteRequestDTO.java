package com.example.runity.DTO.route;

import lombok.Data;

@Data
public class RouteRequestDTO {
    private DestinationDTO startPoint;
    private DestinationDTO endPoint;
    private Long routineId;
}
