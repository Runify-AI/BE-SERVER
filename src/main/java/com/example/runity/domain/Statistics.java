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

    // 종합 통계 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long statisticsId;

    // 유저 아이디(User 외래키)
    @Column(nullable = false)
    private Long userId;

    // 달린 총 거리
    @Column(nullable = false)
    private Float totalDistance;

    // 달린 총 시간
    @Column(nullable = false)
    private LocalTime totalRunTime;
}
