package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RealTimeRunning {

    // 실시간 러닝 세션 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long runningSessionId;

    // 러닝 기록 ID ( 테이블 외래키)
    @Column(nullable = false)
    private Long recordId;

    // 경로 ID (Route 테이블 외래키)
    @Column(nullable = false)
    private Long routeId;

    // 러닝 종료 시각
    @Column
    private LocalDateTime endTime;

    // 러닝 완료 여부 (필수 값)
    @Column(nullable = false)
    private Boolean isCompleted;

    // 포기 이유
    @Column
    private String giveUpReason;

    // 추천 경로 사용 여부
    @Column
    private Boolean isRecommended;

    // 페이스 피드백
    @Column
    private String paceFeedback;

    // 평균 정지 시간
    @Column
    private Float avgStopTime;

    // 평균 속도
    @Column
    private Float avgSpeed;

    // 경과 시간(총 러닝 시간)
    @Column
    private LocalTime elapsedTime;


    // 피드백에 활용되는 데이터
    // 총 달린 거리
    @Column
    private float distance;

    // 러닝 지속 시간
    @Column
    private Float duration;

    // 평균 페이스
    @Column
    private Float avgPace;

    // 정지 횟수
    @Column
    private Integer stopCount;

    // 주 피드백 내용
    @Column
    private String feedback_main;

    // 피드백 조언
    @Column
    private String feedback_advice;

    // 초기 속도 편차
    @Column
    private Double feedback_earlySpeedDeviation;

    // 집중도 점수
    @Column
    private Integer focusScore;

    // 노력 수준
    @Column
    private Integer effortLevel;

    // 기타 코멘트/메모
    @Column
    private String comment;
}
