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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    @Column(name = "indexId")
    private int indexId;

    private double similarity;
    private double paceScore;
    private double finalScore;
    private double recommendedPace;
    private int expectedTime;

    @Builder.Default
    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathCoordinate> coordinates = new ArrayList<>();;

    @Builder.Default
    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathFeature> features = new ArrayList<>();;

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
