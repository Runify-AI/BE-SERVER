package com.example.runity.DTO.route;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponseDTO {
    private int pathId;
    private FeatureDTO feture;
    private RecommendDTO recommend;
    private List<List<Double>> coord;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class FeatureDTO {
        private FeatureDetail park;
        private FeatureDetail river;
        private FeatureDetail amenity;
        private FeatureDetail cross;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class FeatureDetail {
        private int count;
        private double area;
        private double ratio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecommendDTO {
        private double similarity;
        private double pace_score;
        private double final_score;
        private double recommended_pace;
        private int expected_time;
    }
}