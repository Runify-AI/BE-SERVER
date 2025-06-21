package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "paths")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double originLat;
    private double originLon;

    private double destinationLat;
    private double destinationLon;

    private double length;       // 총 거리 (meters)
    private double duration;     // 예상 시간 (minutes)
    private double targetPace;   // 목표 페이스 (분/km)
    private int recommendScore;  // 추천 점수

    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathCoordinate> coordinates;

    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathFeature> features;
}
