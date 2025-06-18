package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long statisticsId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Float totalDistance;

    @Column(nullable = false)
    private LocalTime totalRunTime;
}
