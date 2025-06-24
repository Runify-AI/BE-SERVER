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
import org.springframework.web.reactive.function.client.WebClient;

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
    private final PreferenceRepository preferenceRepository;

    private final WebClient aiWebClient;

    /**
     * AI 경로 추천 요청
     */
    public RecommendationResponseDTO generateRecommendation(String startAddr, String endAddr, RecommendationRequestDTO payload) {
        System.out.println("[AI 요청 시작]");
        System.out.println("시작 주소: " + startAddr);
        System.out.println("종료 주소: " + endAddr);
        System.out.println("Payload: " + payload);

        return aiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/route/")
                        .queryParam("start_address", startAddr)
                        .queryParam("end_address", endAddr)
                        .build())
                .bodyValue(payload)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(errorBody -> {
                                    System.out.println("[AI 요청 실패]");
                                    System.out.println("상태 코드: " + response.statusCode());
                                    System.out.println("에러 본문: " + errorBody);
                                    return new RuntimeException("AI 서버 오류: " + response.statusCode() + " - " + errorBody);
                                })
                                .doOnNext(json -> {
                                    System.out.println("[AI 응답 원문]");
                                    System.out.println(json);
                                })
                )
                .bodyToMono(RecommendationResponseDTO.class)
                .block();
    }

    /**
     * AI 추천 경로 저장
     */
    public void saveRecommendationResults(Long routeId, RecommendationResponseDTO response) {
        List<RecommendedPathsDTO> recommendations = response.getPaths();

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid routeId: " + routeId));

        int nextIndexId = pathRepository.findMaxIndexIdByRouteId(routeId) != null
                ? pathRepository.findMaxPathIdByRouteId(routeId) + 1
                : 1;

        for (RecommendedPathsDTO dto : recommendations) {
            if (dto.getRecommend() == null || dto.getCoord() == null || dto.getFeture() == null) {
                continue;
            }

            Path path = Path.builder()
                    .indexId(nextIndexId++)
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

            // 기본 선택 경로 설정
            if (dto.getPathId() == 0) {
                route.setSelectedPathId(path.getIndexId());
                routeRepository.save(route);
            }
        }
    }

    /**
     * 경로별 요소(Feature) 정보 저장
     */
    private PathFeature buildFeature(Path path, FeatureType type, RecommendedPathsDTO.FeatureDetail detail) {
        return PathFeature.builder()
                .path(path)
                .featureType(type)
                .count(detail.getCount())
                .area(detail.getArea())
                .ratio(detail.getRatio())
                .build();
    }

    /**
     * 유저가 선택한 추천 경로 저장
     */
    public void selectRecommendedPath(String token, Long routeId, int selectedPathId) {
        Long userId = jwtUtil.getUserId(token);

        Route route = routeRepository.findById(routeId)
                .filter(r -> r.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("권한이 없거나 존재하지 않는 경로입니다."));

        route.setSelectedPathId(selectedPathId);
        routeRepository.save(route);
    }

    /**
     * AI 추천 요청용 DTO 생성
     */
    public RecommendationRequestDTO generateRecommendations(String token, Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid routeId: " + routeId));
        Long userId = route.getUser().getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        // 유저 선호 정보 조회
        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("유저의 선호 설정이 존재하지 않습니다."));

        // 유저 프로필 생성
        RecommendationRequestDTO.UserProfile userProfile = RecommendationRequestDTO.UserProfile.builder()
                .running_type(user.getRunningType().name())
                .height(user.getHeight())
                .weight(user.getWeight())
                .preferences(RecommendationRequestDTO.Preferences.builder()
                        .preferencePlace(preference.getPreferencePlaces().stream().map(Enum::name).toList())
                        .preferenceRoute(preference.getPreferenceRoutes().stream().map(Enum::name).toList())
                        .preferenceAvoid(preference.getPreferenceAvoids().stream().map(Enum::name).toList())
                        .preferenceEtc(preference.getPreferenceEtcs().stream().map(Enum::name).toList())
                        .build())
                .build();

        // 날씨 정보
        WeatherDTO weather = weatherService.getWeather("Daegu");

        // 최근 러닝 기록 2일치 수집
        List<DailyRunningRecord> recentRecords = dailyRunningRecordRepository.findTop2ByUserIdOrderByDateDesc(userId);
        List<RecommendationRequestDTO.HistoryDTO> historyList = recentRecords.stream()
                .flatMap(r -> {
                    RunningSessionSummaryDTO summary = runningHistoryService.getDailyRecord(token, r.getDate());
                    return summary.getRunningSessionDTO().stream()
                            .map(RunningSessionDTO::getRunningHistoryDTO)
                            .map(dto -> RecommendationRequestDTO.HistoryDTO.builder()
                                    .routeId(dto.getRouteId())
                                    .date(r.getDate())
                                    .distance(dto.getDistance())
                                    .averagePace(dto.getAveragePace())
                                    .stopCount(dto.getStopCount())
                                    .feedbackSummary(dto.getFeedbackSummaryDTO())
                                    .focusScore(dto.getFocusScore())
                                    .effortLevel(dto.getEffortLevel())
                                    .comment(dto.getComment())
                                    .build());
                })
                .sorted(Comparator.comparing(RecommendationRequestDTO.HistoryDTO::getDate).reversed())
                .limit(2)
                .collect(Collectors.toList());

        // 최종 요청 객체 구성
        return RecommendationRequestDTO.builder()
                .user_profile(userProfile)
                .weather(weather)
                .history(historyList)
                .build();
    }
}