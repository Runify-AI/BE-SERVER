package com.example.runity.service;

import com.example.runity.DTO.RunningSettingsResponse;
import com.example.runity.domain.Route;
import com.example.runity.repository.RouteChoiceRepository;
import com.example.runity.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RunningSettingServiceImpl implements RunningSettingService {

        private final RouteRepository routeRepository;
        private final RouteChoiceRepository routeChoiceRepository;
        //private final AiPaceService aiPaceService; // TODO: AI 호출하는 서비스

        public RunningSettingsResponse getRunningSetting(Long routeId) {
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new RuntimeException("Route not found"));

            /* TODO: 좌표 리스트 불러오기
            List<String> routePoints = routeChoiceRepository
                    .findAllByRoute_RouteId(routeId)
                    .stream()
                    .map(RouteChoice::getCoordinate)
                    .collect(Collectors.toList());
             */
            List<String> routePoints = null;

            String duration = formatDuration(route.getEstimatedTime());
            String startTime = LocalTime.now().toString();
            LocalTime estimatedLocalTime = route.getEstimatedTime().toLocalTime();
            String estimatedEndTime = LocalTime.now().plusSeconds(estimatedLocalTime.toSecondOfDay()).toString();

            // TODO: AI 호출하여 페이스 생성
            //String pace = aiPaceService.generatePace(route);

            // 생성한 pace를 Route에 저장하고 업데이트 (임시 값)
            double pace = 3.5;
            //route.setPace(pace);
            //routeRepository.save(route);



            return RunningSettingsResponse.builder()
                    .routePoints(routePoints)
                    .duration(duration)
                    .startTime(startTime)
                    .estimatedEndTime(estimatedEndTime)
                    .startPoint(route.getStartPoint())
                    .endPoint(route.getEndPoint())
                    .pace(String.valueOf(pace))
                    .build();
        }

        private String formatDuration(Time time) {
            return time.toString(); // 혹은 원하는 형식으로 포맷
        }

    @Override
    public RunningSettingsResponse getRunningSettings(Long routeId) {
        return null;
    }
}
