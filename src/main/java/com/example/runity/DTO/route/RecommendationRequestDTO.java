package com.example.runity.DTO.route;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.DTO.runningTS.FeedbackSummaryDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestDTO {
    private UserProfile user_profile;
    private List<HistoryDTO> history;
    private WeatherDTO weather;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HistoryDTO {
        private Long routeId;                             // 루트 아이디
        private LocalDate date;
        private Float distance;                      // 총 거리
        private Float duration;
        private Float averagePace;                       // 평균 페이스
        private Integer stopCount;
        private FeedbackSummaryDTO feedbackSummary;
        private Integer focusScore;
        private Integer effortLevel;                      // 힘듦 정도
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfile {
        private String running_type;
        private Double height;
        private Double weight;
        private Preferences preferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Preferences {
        private List<String> preferencePlace;
        private List<String> preferenceRoute;
        private List<String> preferenceAvoid;
        private List<String> preferenceEtc;
    }
}
