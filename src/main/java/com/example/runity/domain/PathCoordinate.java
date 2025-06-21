package com.example.runity.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "path_coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathCoordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double lat;
    private double lon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    private Path path;

    @Column(nullable = false)
    private int sequence; // 좌표 순서를 위해 필요
}
