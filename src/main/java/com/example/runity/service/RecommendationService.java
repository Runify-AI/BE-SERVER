// RecommendationService.java
package com.example.runity.service;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.DTO.history.RunningSessionDTO;
import com.example.runity.DTO.history.RunningSessionSummaryDTO;
import com.example.runity.DTO.route.*;
import com.example.runity.domain.*;
import com.example.runity.enums.FeatureType;
import com.example.runity.repository.*;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final PathRepository pathRepository;
    private final PathCoordinateRepository pathCoordinateRepository;
    private final PathFeatureRepository pathFeatureRepository;
    private final RouteRepository routeRepository;
    private final JwtUtil jwtUtil;
    private final WeatherService weatherService;
    private final UserRepository userRepository;
    private final RunningHistoryService runningHistoryService;
    private final DailyRunningRecordRepository dailyRunningRecordRepository;

    public List<RecommendationResponseDTO> generateRecommendation(RecommendationRequestDTO request) {
        // TODO: AI 추천 생성 로직 구현
        return List.of();
    }

    public void saveRecommendationResults(Long routeId, List<RecommendationResponseDTO> recommendations) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid routeId: " + routeId));

        int nextPathId = pathRepository.findMaxPathIdByRouteId(routeId) != null
                ? pathRepository.findMaxPathIdByRouteId(routeId) + 1
                : 1;

        for (RecommendationResponseDTO dto : recommendations) {
            Path path = Path.builder()
                    .pathId(nextPathId++)
                    .similarity(dto.getRecommend().getSimilarity())
                    .paceScore(dto.getRecommend().getPace_score())
                    .finalScore(dto.getRecommend().getFinal_score())
                    .recommendedPace(dto.getRecommend().getRecommended_pace())
                    .expectedTime(dto.getRecommend().getExpected_time())
                    .route(route)
                    .build();
            pathRepository.save(path);

            List<PathCoordinate> coordinates = dto.getCoord().stream()
                    .map(coord -> PathCoordinate.builder()
                            .lat(coord.get(0))
                            .lon(coord.get(1))
                            .path(path)
                            .sequence(dto.getCoord().indexOf(coord))
                            .build())
                    .collect(Collectors.toList());
            pathCoordinateRepository.saveAll(coordinates);

            pathFeatureRepository.saveAll(List.of(
                    buildFeature(path, FeatureType.PARK, dto.getFeture().getPark()),
                    buildFeature(path, FeatureType.RIVER, dto.getFeture().getRiver()),
                    buildFeature(path, FeatureType.AMENITY, dto.getFeture().getAmenity()),
                    buildFeature(path, FeatureType.CROSS, dto.getFeture().getCross())
            ));

            // 초기 선택된 pathId 저장
            if (dto.getPathId() == 0) {
                route.setSelectedPathId(path.getId());
                routeRepository.save(route);
            }
        }
    }

    private PathFeature buildFeature(Path path, FeatureType type, RecommendationResponseDTO.FeatureDetail detail) {
        return PathFeature.builder()
                .path(path)
                .featureType(type)
                .count(detail.getCount())
                .area(detail.getArea())
                .ratio(detail.getRatio())
                .build();
    }

    public void selectRecommendedPath(String token, Long routeId, Long selectedPathId) {
        Long userId = jwtUtil.getUserId(token);
        Route route = routeRepository.findById(routeId)
                .filter(r -> r.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("권한이 없거나 존재하지 않는 경로입니다."));
        route.setSelectedPathId(selectedPathId);
        routeRepository.save(route);
    }

    public RecommendationRequestDTO generateRecommendations(String token, Long routeId) {
        //Long userId = jwtUtil.getUserId(token);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid routeId: " + routeId));
        Long userId = route.getUser().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        // 1. user_profile 구성
        RecommendationRequestDTO.UserProfile userProfile = RecommendationRequestDTO.UserProfile.builder()
                .running_level(user.getRunningType().name())
                .preferences(RecommendationRequestDTO.Preferences.builder()
                        .location(List.of("park", "amenity")) // TODO: 유저 선호 설정 도입 시 변경
                        .avoid(List.of("crowd", "lazy"))
                        .path(List.of("speed"))
                        .build())
                .build();

        // 2. 날씨
        WeatherDTO weather = weatherService.getWeather("Seoul");

        // 3. 러닝 기록
        List<DailyRunningRecord> recentRecords = dailyRunningRecordRepository.findTop2ByUserIdOrderByDateDesc(userId);
        List<RecommendationRequestDTO.HistoryDTO> historyList = recentRecords.stream()
                .flatMap(r -> {
                    RunningSessionSummaryDTO summary = runningHistoryService.getDailyRecord(token, r.getDate());
                    return summary.getRunningSessionDTO().stream()
                            .map(RunningSessionDTO::getRunningHistoryDTO)
                            .map(dto -> RecommendationRequestDTO.HistoryDTO.builder()
                                    .routeId(dto.getRouteId())
                                    .date(r.getDate())  // 여기서 날짜 유지
                                    .totalDistance(dto.getTotalDistance())
                                    .averagePace(dto.getAveragePace())
                                    .effortLevel(dto.getEffortLevel())
                                    .comment(dto.getComment())
                                    .runningTrackPoint(dto.getRunningTrackPoint())
                                    .build());
                })
                .sorted(Comparator.comparing(RecommendationRequestDTO.HistoryDTO::getDate).reversed())
                .limit(2)
                .collect(Collectors.toList());

        return RecommendationRequestDTO.builder()
                .user_profile(userProfile)
                .weather(weather)
                .history(historyList)
                .build();
    }
}
