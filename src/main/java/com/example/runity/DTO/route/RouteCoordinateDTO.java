package com.example.runity.DTO.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
public class RouteCoordinateDTO {
    private Double latitude;
    private Double longitude;
}
