package com.example.runity.DTO.route;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.DTO.runningTS.FeedbackSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestDTO {
    // 시작점, 도착점
    private DestinationDTO startAddr;
    private DestinationDTO endAddr;

    @JsonProperty("user_profile")
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
        private FeedbackSummary feedbackSummary;
        private Integer focusScore;
        private Integer effortLevel;                      // 힘듦 정도
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfile {
        private String runningType;
        private Double height;
        private Double weight;
        private Preferences preferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Preferences {
        private List<String> preferencePlaces;
        private List<String> preferenceRoutes;
        private List<String> preferenceAvoids;
        private List<String> preferenceEtcs;
    }
}
