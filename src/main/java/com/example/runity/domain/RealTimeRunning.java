package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
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
    private Instant endTime;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column
    private String giveUpReason;

    @Column
    private Integer effortLevel;

    @Column
    private Boolean isRecommended;

    @Column
    private String comment;

    @Column
    private String paceFeedback;

    @Column
    private LocalTime avgStopTime;

    @Column
    private Float avgPace;

    @Column
    private Float avgSpeed;

    @Column
    private float distance;

    @Column
    private LocalTime runTime;

}
