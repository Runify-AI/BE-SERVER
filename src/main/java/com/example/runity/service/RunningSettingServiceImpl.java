package com.example.runity.service;

import com.example.runity.DTO.RunningSettingsResponse;
//import com.example.runity.domain.Route;
import com.example.runity.domain.Routine;
//import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.RoutineRepository;
//import com.example.runity.repository.AIRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningSettingServiceImpl implements RunningSettingService {
    /*
        private final RouteRepository routeRepository;
        private final RouteChoiceRepository routeChoiceRepository;
        private final AiPaceService aiPaceService; // AI 호출하는 서비스

        public RunningSettingsResponse getRunningSetting(Long routeId) {
            RouteMatcher.Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RuntimeException("Route not found"));

            List<String> routePoints = routeChoiceRepository
                    .findAllByRoute_RouteId(routeId)
                    .stream()
                    .map(RouteChoice::getCoordinate)
                    .collect(Collectors.toList());

            String duration = formatDuration(route.getEstimatedTime());
            String startTime = LocalTime.now().toString();
            String estimatedEndTime = LocalTime.now().plusSeconds(route.getEstimatedTime().toSecondOfDay()).toString();

            // AI 호출하여 페이스 생성
            String pace = aiPaceService.generatePace(route); // 예: "5:30/km"

            // (선택) 생성한 pace를 Route에 저장하고 업데이트
            route.setPace(pace);
            routeRepository.save(route);

            return new RunningSettingsResponse(
                    routePoints,
                    duration,
                    startTime,
                    estimatedEndTime,
                    route.getStartPoint(),
                    route.getEndPoint(),
                    pace
            );
        }

        private String formatDuration(Time time) {
            return time.toString(); // 혹은 원하는 형식으로 포맷
        }
     */
    @Override
    public RunningSettingsResponse getRunningSettings(Long routeId) {
        return null;
    }
}
