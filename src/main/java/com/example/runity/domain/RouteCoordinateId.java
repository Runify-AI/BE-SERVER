package com.example.runity.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RouteCoordinateId implements Serializable {
    private Long routeId;
    private Long coordinateId;  // 단순히 PK 역할
}
