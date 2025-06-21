package com.example.runity.service;

import com.example.runity.DTO.route.RouteCoordinateDTO;
import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.domain.Route;
import com.example.runity.repository.RouteCoordinateRepository;
import com.example.runity.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningSettingServiceImpl implements RunningSettingService {

    private final RouteRepository routeRepository;
    private final RouteCoordinateRepository routeCoordinateRepository;
    //private final AiPaceService aiPaceService; // TODO: AI 호출하는 서비스

    @Override
    public RunningSettingsResponse getRunningSettings(Long routeId) {
        System.out.println(routeId);
        Route route = routeRepository.findByRouteId(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<RouteCoordinateDTO> routePoints = routeCoordinateRepository
                .findByRoute_RouteId(routeId)
                .stream()
                .map(coord -> new RouteCoordinateDTO(coord.getLatitude(), coord.getLongitude()))
                .collect(Collectors.toList());

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
                .targetPace(String.valueOf(pace))
                .build();
    }

    private String formatDuration(LocalDateTime time) {
        return time.toString(); // 혹은 원하는 형식으로 포맷
    }
}