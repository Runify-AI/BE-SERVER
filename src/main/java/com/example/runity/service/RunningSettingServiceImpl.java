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

    /**
     * 주어진 routeId로부터 러닝 세팅 정보를 조회
     */
    @Override
    public RunningSettingsResponse getRunningSettings(Long routeId) {
        Route route = routeRepository.findById(routeId).orElse(null);

        // route 없으면 기본값 반환
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
            // 선택된 path의 좌표 조회
            routePoints = pathCoordinateRepository.findAllByPathIdOrderBySequenceAsc(selectedPathId)
                    .stream()
                    .map(pc -> new RouteCoordinateDTO(pc.getLat(), pc.getLon()))
                    .collect(Collectors.toList());

            // 추천 페이스 조회
            pathRepository.findById(selectedPathId).ifPresent(path -> {
                Double pace = path.getRecommendedPace();
                targetPaceRef.set(pace != null ? String.format("%.2f", pace) : null);
            });
        }
        String targetPace = targetPaceRef.get();

        // 현재 시간 구하기
        LocalTime now = LocalTime.now();
        String startTime = now.toString();

        // duration 문자열 가져오기 (null 가능)
        String duration = route.getEstimatedTime() != null ? route.getEstimatedTime().toString() : null;

        // 예상 종료 시간 계산
        String estimatedEndTime = null;
        if (duration != null) {
            try {
                estimatedEndTime = now.plusSeconds(parseDurationToSeconds(duration)).toString();
            } catch (Exception e) {
                // 파싱 실패 시 무시
            }
        }

        // 응답 DTO 생성 및 반환
        return RunningSettingsResponse.builder()
                .routePoints(routePoints)
                .duration(duration)
                .startTime(startTime)
                .estimatedEndTime(estimatedEndTime != null ? estimatedEndTime : null)
                .startPoint(route.getStartPoint())
                .endPoint(route.getEndPoint())
                .targetPace(targetPace)
                .build();
    }

    /**
     * "HH:mm:ss" 형식의 문자열을 초 단위로 변환
     */
    private long parseDurationToSeconds(String duration) {
        return LocalTime.parse(duration).toSecondOfDay();
    }
}
