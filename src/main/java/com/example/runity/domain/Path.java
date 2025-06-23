package com.example.runity.domain;

import com.example.runity.DTO.route.RecommendedPathsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "paths")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 경로 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;  // 이 경로가 속한 Route 엔티티

    @Column(name = "indexId")
    private int indexId;  // 경로 인덱스 번호

    private double similarity;      // 유사도 점수
    private double paceScore;       // 페이스 점수
    private double finalScore;      // 최종 점수
    private double recommendedPace; // 추천 페이스
    private int expectedTime;       // 예상 소요 시간(초)

    @Builder.Default
    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathCoordinate> coordinates = new ArrayList<>();  // 경로 좌표 리스트

    @Builder.Default
    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathFeature> features = new ArrayList<>();  // 경로 관련 특성 리스트

    /**
     * Path 엔티티를 RecommendedPathsDTO로 변환
     */
    public RecommendedPathsDTO toRecommendationDTO() {
        return RecommendedPathsDTO.builder()
                .pathId(this.indexId)
                .feture(buildFeatureDTO(this.features))
                .recommend(RecommendedPathsDTO.RecommendDTO.builder()
                        .similarity(this.similarity)
                        .pace_score(this.paceScore)
                        .final_score(this.finalScore)
                        .recommended_pace(this.recommendedPace)
                        .expected_time(this.expectedTime)
                        .build())
                .coord(this.coordinates.stream()
                        .sorted(Comparator.comparingInt(PathCoordinate::getSequence))
                        .map(c -> List.of(c.getLat(), c.getLon()))
                        .toList())
                .build();
    }

    /**
     * PathFeature 리스트를 RecommendedPathsDTO.FeatureDTO로 변환
     */
    private RecommendedPathsDTO.FeatureDTO buildFeatureDTO(List<PathFeature> features) {
        RecommendedPathsDTO.FeatureDTO.FeatureDTOBuilder builder = RecommendedPathsDTO.FeatureDTO.builder();

        if(features != null) {
            for (PathFeature feature : features) {
                RecommendedPathsDTO.FeatureDetail detail = new RecommendedPathsDTO.FeatureDetail(
                        feature.getCount(),
                        feature.getArea() != null ? feature.getArea() : 0.0,
                        feature.getRatio() != null ? feature.getRatio() : "0.0"
                );

                switch (feature.getFeatureType()) {
                    case PARK -> builder.park(detail);
                    case RIVER -> builder.river(detail);
                    case AMENITY -> builder.amenity(detail);
                    case CROSS -> builder.cross(detail);
                }
            }
        }

        return builder.build();
    }
}
