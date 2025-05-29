package com.example.runity.tracking;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "real_time_tracking")
public class RealTimeTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackingId;

    private Long routeId;
    private String currentLocation;
    private Float speed;
    private Integer heartbeat;
    private Boolean outOfRoute;
    private LocalDateTime createAt;

    // getters, setters
}
