package com.example.runity.DTO.route;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LocationDTO {
    private Double lat;
    private Double lon;
}
