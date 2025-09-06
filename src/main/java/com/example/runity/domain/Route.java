package com.example.runity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="routes")
@Getter
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {
    // 경로 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long routeId;
    // 실시간 러닝 ID
    private Long sessionId;
    // 러닝 기록 ID
    private Long recordId;
    // 유저 ID
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    // 출발지점
    @Setter
    @Column(nullable = false)
    private String startPoint;
    // 목적지점
    @Setter
    @Column(nullable = false)
    private String endPoint;
    // 예상시간
    @Setter
    @Column(nullable = false)
    private Time estimatedTime;
    // 거리
    @Setter
    @Column(nullable = false)
    private Float distance;
    // 경로 생성
    @Setter
    @Column(nullable = false)
    private LocalDateTime createdAt;
    // 중복 저장 허용
    @Setter
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteChoice> routeChoices = new ArrayList<>();

    public Route(User user, String startPoint, String endPoint, Time estimatedTime, Float distance) {
        this.user = user;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String startPoint, String endPoint, Time estimatedTime, Float distance) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
    }
}
