package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRunningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long recordId;  // 기록 고유 ID

    @Column(nullable = false)
    private Long userId;  // 사용자 ID

    @Column(nullable = false)
    private Float totalDistance;  // 총 주행 거리

    @Column(nullable = false)
    private LocalTime totalRunTime;  // 총 주행 시간

    @Column
    private LocalDate date;  // 기록 날짜

    @Column
    private Integer runCount;  // 실행 횟수 (러닝 세션 수)

    @Column
    private Float avgSpeed;  // 평균 속도

    @Column(name = "weather")
    private String weather;  // 날씨 상태

    @Column(name = "temperature")
    private Float temperature;  // 기온

    @Column(name = "humidity")
    private Float humidity;  // 습도
}
