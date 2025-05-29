package com.example.runity.tracking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RealTimeTrackingEntity {
    @Id
    private Long trackingId;

    private Long routeId;
    private String currentLocation;
    private Float speed;
    private Integer heartbeat;
    private Boolean outOfRoute;
    private LocalDateTime createAt;

    // getters, setters
}
