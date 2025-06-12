package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RealTimeTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long trackingId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String currentLocation;

    @Column(nullable = false)
    private Float speed;

    @Column(nullable = false)
    private LocalDateTime createAt;
}
