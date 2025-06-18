package com.example.runity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weather")
@Getter
@Setter
public class WeatherProperties {
    private String apiKey;
    private String baseUrl;
    private String defaultLocation;
    private String units;
}
