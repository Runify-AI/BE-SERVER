package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeatherDTO {
    private String weather;
    private Double temperature;
    private Double humidity;
}
