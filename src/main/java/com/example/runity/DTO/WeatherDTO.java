package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeatherDTO {
    private Double temperature;
    private Double humidity;
    private String condition;
}
