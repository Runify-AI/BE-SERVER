package com.example.runity.domain;

import com.example.runity.DTO.route.RecommendationResponseDTO;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private Route route;

    private int pathId;
    private double similarity;
    private double paceScore;
    private double finalScore;
    private double recommendedPace;
    private int expectedTime;

    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathCoordinate> coordinates;

    @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathFeature> features;

    public RecommendationResponseDTO toRecommendationDTO() {
        return RecommendationResponseDTO.builder()
                .pathId(this.pathId)
                .feture(buildFeatureDTO(this.features))
                .recommend(RecommendationResponseDTO.RecommendDTO.builder()
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

    private RecommendationResponseDTO.FeatureDTO buildFeatureDTO(List<PathFeature> features) {
        RecommendationResponseDTO.FeatureDTO.FeatureDTOBuilder builder = RecommendationResponseDTO.FeatureDTO.builder();

        for (PathFeature feature : features) {
            RecommendationResponseDTO.FeatureDetail detail = new RecommendationResponseDTO.FeatureDetail(
                    feature.getCount(),
                    feature.getArea() != null ? feature.getArea() : 0.0,
                    feature.getRatio() != null ? feature.getRatio() : 0.0
            );

            switch (feature.getFeatureType()) {
                case PARK -> builder.park(detail);
                case RIVER -> builder.river(detail);
                case AMENITY -> builder.amenity(detail);
                case CROSS -> builder.cross(detail);
            }
        }

        return builder.build();
    }
}
