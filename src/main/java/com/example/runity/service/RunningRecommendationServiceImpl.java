package com.example.runity.service;

import com.example.runity.DTO.*;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
import com.example.runity.domain.Route;
import com.example.runity.domain.User;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RunningRecommendationServiceImpl implements RunningRecommendationService{

    private final UserRepository userRepository;
    private final DailyRunningRecordRepository dailyRecordRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final RouteRepository routeRepository;
    private final WeatherService weatherService;

    @Override
    public RunningPerformanceDTO evaluateRunningPerformance(Long userId) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보 없음"));

        UserProfileDTO userProfile = new UserProfileDTO(user.getHeight(), user.getWeight(), user.getRunningType());

        // 2. 최근 3일 러닝 데이터 가져오기
        List<DailyRunningRecord> recentRecords = dailyRecordRepository.findTop3ByUserIdOrderByDateDesc(userId);

        List<UserFeedbackDTO> feedbackHistory = new ArrayList<>();
        List<Float> paceList = new ArrayList<>();
        List<Float> speedList = new ArrayList<>();
        float totalStopTime = 0;

        for (DailyRunningRecord record : recentRecords) {
            List<RealTimeRunning> runList = realTimeRunningRepository.findByRecordId(record.getRecordId());

            for (RealTimeRunning run : runList) {
                // Feedback 수집
                feedbackHistory.add(new UserFeedbackDTO(
                        record.getDate(),
                        run.getComment() != null ? run.getComment() : "",
                        run.getEffortLevel() != null ? run.getEffortLevel() : 0
                ));
                // 평균 pace/speed 수집
                if (run.getAvgPace() != null) paceList.add(run.getAvgPace());
                if (run.getAvgSpeed() != null) speedList.add(run.getAvgSpeed());

                // 정지 시간 수집
                if (run.getAvgStopTime() != null) totalStopTime += run.getAvgStopTime();
            }
        }

        float avgPauseTime = recentRecords.size() > 0 ? totalStopTime / recentRecords.size() : 0;

        // 리스트 자르기: 최신 3개만
        if (feedbackHistory.size() > 3) {
            feedbackHistory = feedbackHistory.subList(0, 3);
        }
        if (paceList.size() > 3) {
            paceList = paceList.subList(0, 3);
        }
        if (speedList.size() > 3) {
            speedList = speedList.subList(0, 3);
        }

        // 3. 루트 정보 수집 (가정: 유저가 선택한 루트를 저장했다고 가정)
        /*
        Route route = routeRepository.findLatestSelectedRouteByUserId(userId) // 임시 메서드, 미정
                .orElse(new Route(5.0f, true, "moderate")); // 기본값

        RouteInfoDTO routeInfo = new RouteInfoDTO(route.getDistance(), route.isLoop(), route.getElevation());
         */

        // 4. 환경 정보 조회
        WeatherDTO weatherDTO = weatherService.getWeather("Daegu");

        return new RunningPerformanceDTO(
                userProfile,
                feedbackHistory,
                avgPauseTime,
                paceList,
                speedList,
                //routeInfo,
                weatherDTO
        );
    }
}
