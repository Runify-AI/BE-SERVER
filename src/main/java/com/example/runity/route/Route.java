package com.example.runity.route;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    private Long userId;
    private String startPoint;
    private String endPoint;
    private Time estimatedTime;
    private Float distance;
    private LocalDateTime createAt;


    // Getter/Setter 생략
}
