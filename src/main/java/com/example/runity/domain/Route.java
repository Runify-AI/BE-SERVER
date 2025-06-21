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
    @Builder.Default
    @Setter
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteChoice> routeChoices = new ArrayList<>();
    // 좌표 리스트
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "route_coordinates", joinColumns = @JoinColumn(name = "route_id"))
    private Set<Coordinate> coordinates = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;
    public Routine getRoutine() {
        return routine;
    }


    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private double latitude;
        private double longitude;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinate)) return false;
            Coordinate that = (Coordinate) o;
            return Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(latitude, longitude);
        }
    }

    public Route(User user, String startPoint, String endPoint, Time estimatedTime, Float distance) {
        this.user = user;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
        //this.createdAt = LocalDateTime.now();
    }

    public void update(String startPoint, String endPoint, Time estimatedTime, Float distance) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
    }

    public List<RouteChoice> getRouteChoices() {
        if (routeChoices == null) {
            routeChoices = new ArrayList<>();
        }
        return routeChoices;
    }

    public Set<Coordinate> getCoordinates() {
        if (coordinates == null) {
            coordinates = new HashSet<>();
        }
        return coordinates;
    }
}
