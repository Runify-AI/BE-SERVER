package com.example.runity.service;

import com.example.runity.DTO.route.RouteCoordinateDTO;
import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.domain.Route;
import com.example.runity.repository.PathCoordinateRepository;
import com.example.runity.repository.PathRepository;
import com.example.runity.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningSettingServiceImpl implements RunningSettingService {

    private final RouteRepository routeRepository;
    private final PathRepository pathRepository;
    private final PathCoordinateRepository pathCoordinateRepository;

    @Override
    public RunningSettingsResponse getRunningSettings(Long routeId) {
        Route route = routeRepository.findById(routeId).orElse(null);

        // route가 없으면 null 필드 채워서 반환
        if (route == null) {
            return RunningSettingsResponse.builder()
                    .routePoints(List.of())
                    .duration(null)
                    .startTime(LocalTime.now().toString())
                    .estimatedEndTime(null)
                    .startPoint(null)
                    .endPoint(null)
                    .build();
        }

        Long selectedPathId = route.getSelectedPathId();
        List<RouteCoordinateDTO> routePoints = List.of();

        AtomicReference<String> targetPaceRef = new AtomicReference<>(null);
        if (selectedPathId != null) {
            // path 좌표
            routePoints = pathCoordinateRepository.findAllByPathIdOrderBySequenceAsc(selectedPathId)
                    .stream()
                    .map(pc -> new RouteCoordinateDTO(pc.getLat(), pc.getLon()))
                    .collect(Collectors.toList());

            // recommendedPace 조회
            pathRepository.findById(selectedPathId).ifPresent(path -> {
                Double pace = path.getRecommendedPace();
                targetPaceRef.set(pace != null ? String.format("%.2f", pace) : null);
            });
        }
        String targetPace = targetPaceRef.get();

        // [2] 현재 시간
        LocalTime now = LocalTime.now();
        String startTime = now.toString();

        // [3] duration (Route.duration은 "HH:mm:ss" 형식의 String으로 가정)
        String duration = route.getEstimatedTime() != null ? route.getEstimatedTime().toString() : null;

        // [4] 예상 종료 시간 계산
        String estimatedEndTime = null;
        if (duration != null) {
            try {
                estimatedEndTime = now.plusSeconds(parseDurationToSeconds(duration)).toString();
            } catch (Exception e) {
                // parse 실패 시 null 유지
            }
        }
        // [5] 응답 DTO 구성
        return RunningSettingsResponse.builder()
                .routePoints(routePoints)
                .duration(duration)
                .startTime(startTime)
                .estimatedEndTime(estimatedEndTime != null ? estimatedEndTime.toString() : null)
                .startPoint(route.getStartPoint())
                .endPoint(route.getEndPoint())
                .targetPace(targetPace)
                .build();
    }

    private long parseDurationToSeconds(String duration) {
        // "HH:mm:ss" 포맷 문자열을 초 단위로 변환
        return LocalTime.parse(duration).toSecondOfDay();
    }
}
