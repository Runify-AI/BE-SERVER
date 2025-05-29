package com.example.runity.route;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Entity
public class RouteEntity {
    @Id
    @GeneratedValue
    private Long routeId;

    private Long userId;
    private String startPoint;
    private String endPoint;
    private Time estimatedTime;
    private Float distance;
    private LocalDateTime createAt;


    // Getter/Setter 생략
}
