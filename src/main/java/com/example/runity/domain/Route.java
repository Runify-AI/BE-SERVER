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
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name="routes")
@Getter
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {
    // 러닝 여부
    @Setter
    @Column(nullable = false)
    private  boolean completed;
    // 경로 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long routeId;
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
    @Column
    private String endPoint;
    // 예상시간
    @Setter
    @Column
    private LocalTime estimatedTime;
    // 거리
    @Setter
    @Column
    private Float distance;
    // 경로 생성
    @Setter
    @Column(nullable = false)
    private LocalDateTime createdAt;
    // 선택된 path의 아이디
    @Setter
    private Long selectedPathId;

    // 중복 저장 허용
    @Builder.Default
    @Setter
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteChoice> routeChoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;
    public Routine getRoutine() {
        return routine;
    }


    public Route(User user, String startPoint, String endPoint) {
        this.user = user;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void update(String startPoint, String endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public List<RouteChoice> getRouteChoices() {
        if (routeChoices == null) {
            routeChoices = new ArrayList<>();
        }
        return routeChoices;
    }
}
