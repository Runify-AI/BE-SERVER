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

    // 경로 특징 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 특징 유형(공원, 강, 교차로 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    // 발생 횟수
    @Column(nullable = false)
    private int count;

    // 면적/영역
    private Double area;

    // 비율
    private String ratio;

    // 경로 (Path 외래키, 지연 로딩, 필수 값)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    private Path path;
}
