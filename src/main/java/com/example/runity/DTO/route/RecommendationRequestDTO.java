package com.example.runity.DTO.route;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.DTO.history.RunningHistoryDetailDTO;
import com.example.runity.DTO.history.RunningSessionSummaryDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
        private Float totalDistance;                      // 총 거리
        private Float averagePace;                       // 평균 페이스
        private Integer effortLevel;                      // 힘듦 정도
        private String comment;

        private List<RunningHistoryDetailDTO> runningTrackPoint;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProfile {
        private String running_level;
        private Preferences preferences;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Preferences {
        private List<String> location;
        private List<String> avoid;
        private List<String> path;
    }
}
