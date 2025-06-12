package com.example.runity.service;

import com.example.runity.DTO.RunningSettingsResponse;
//import com.example.runity.domain.Route;
import com.example.runity.domain.Routine;
//import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.RoutineRepository;
//import com.example.runity.repository.AIRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunningSettingServiceImpl implements RunningSettingService {

    //private final RouteRepository routeRepository;
    private final RoutineRepository routineRepository;
    //private final AIRecommendRepository aiRecommendRepository;

    @Override
    public RunningSettingsResponse getRunningSettings(Long userId) {
        /*
        Route route = routeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 경로 정보가 존재하지 않습니다."));
         */

        Routine routine = routineRepository.findByUserId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 루틴 정보가 존재하지 않습니다."));
        /*
        Double recommendedPace = aiRecommendRepository.findByUserId(userId)
                .map(ai -> ai.getPace())
                .orElse(6.0); // 기본 페이스: 6분/km

         */

        /*
        return RunningSettingsResponse.builder()
                .route(route)
                .routine(routine)
                .recommendedPace(recommendedPace)
                .build();

         */
        return RunningSettingsResponse.builder().build();
    }
}
