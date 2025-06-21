package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route_coordinate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteCoordinate {

    @EmbeddedId
    private RouteCoordinateId id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @MapsId("routeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId", nullable = false)
    private Route route;
}
