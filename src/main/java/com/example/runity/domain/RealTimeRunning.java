package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RealTimeRunning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long runningSessionId;

    @Column(nullable = false)
    private Long recordId;

    @Column(nullable = false)
    private Long routeId;

    @Column
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column
    private String giveUpReason;

    @Column
    private Boolean isRecommended;

    @Column
    private String paceFeedback;

    @Column
    private Float avgStopTime;

    @Column
    private Float avgSpeed;

    @Column
    private LocalTime runTime;


    // 피드백
    @Column
    private float distance;

    @Column
    private Float duration;

    @Column
    private Float avgPace;

    @Column
    private Integer stopCount;

    @Column
    private String feedback_main;

    @Column
    private String feedback_advice;

    @Column
    private Float feedback_earlySpeedDeviation;

    @Column
    private Integer focusScore;

    @Column
    private Integer effortLevel;

    @Column
    private String comment;
}
