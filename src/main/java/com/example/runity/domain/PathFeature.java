package com.example.runity.domain;

import com.example.runity.enums.FeatureType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "path_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    @Column(nullable = false)
    private int count;

    private Double area;

    private String ratio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    private Path path;
}
