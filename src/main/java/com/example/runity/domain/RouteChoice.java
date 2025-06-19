package com.example.runity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "route_choices")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class RouteChoice {
    // 경로 설정 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long choiceId;
    // 경로 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId", nullable = false)
    private Route route;
    // 대중교통 이용
    @Column(nullable = false)
    private boolean usePublicTransport;
    // 완만한 경사
    @Column(nullable = false)
    private boolean preferSafePath;
    // 붐비지 않는 곳
    @Column(nullable = false)
    private boolean avoidCrowdedAreas;
    // 순환
    @Column(nullable = false)
    private boolean cycle;
}
