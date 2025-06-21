package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LocasionDTO {
    private Double lat;
    private Double lon;
}
