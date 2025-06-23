package com.example.runity.service;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.config.WeatherProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherProperties weatherProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 주어진 위치에 대한 날씨 정보를 OpenWeatherMap API로부터 조회
     */
    public WeatherDTO getWeather(String location) {
        String url = String.format("%s?q=%s&appid=%s&units=%s",
                weatherProperties.getBaseUrl(),
                location,
                weatherProperties.getApiKey(),
                weatherProperties.getUnits());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            String weather = root.path("weather").get(0).path("main").asText();
            double temp = root.path("main").path("temp").asDouble();
            double humidity = root.path("main").path("humidity").asDouble();

            return new WeatherDTO(temp, humidity, weather);
        } catch (Exception e) {
            e.printStackTrace();
            return new WeatherDTO(null, null, "정보 없음");
        }
    }
}
