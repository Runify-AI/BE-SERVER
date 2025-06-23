package com.example.runity.service;

import com.example.runity.DTO.history.RunningHistoryDTO;
import com.example.runity.DTO.history.RunningHistoryDetailDTO;
import com.example.runity.DTO.history.RunningSessionDTO;
import com.example.runity.DTO.history.RunningSessionSummaryDTO;
import com.example.runity.DTO.route.LocationDTO;
import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.DTO.runningTS.FeedbackSummary;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
import com.example.runity.domain.RunningPathTS;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RouteRepository;
import com.example.runity.repository.RunningPathTSRepository;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningHistoryServiceImpl implements RunningHistoryService {

    private final DailyRunningRecordRepository dailyRunningRecordRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final RunningPathTSRepository runningPathTSRepository;
    private final RunningSettingService runningSettingService;
    private final RouteRepository routeRepository;
    private final JwtUtil jwtUtil;

    /**
     * 해당 달의 km 리스트 조회
     */
    @Override
    public List<Float> getMonthlyDistances(String token, YearMonth yearMonth) {
        Long userId = jwtUtil.getUserId(token);

        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 1. 해당 월의 기록을 조회
        List<DailyRunningRecord> records = dailyRunningRecordRepository
                .findByUserIdAndDateBetween(userId, startOfMonth, endOfMonth);

        // 2. 날짜별 거리 Map 생성 (기록 있는 날만)
        Map<LocalDate, Float> dateToDistanceMap = records.stream()
                .collect(Collectors.toMap(
                        DailyRunningRecord::getDate,
                        DailyRunningRecord::getTotalDistance
                ));

        // 3. 월 전체 날짜 돌면서 거리 채우기
        int daysInMonth = yearMonth.lengthOfMonth();
        List<Float> result = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            result.add(dateToDistanceMap.getOrDefault(date, 0f));
        }

        return result;
    }


    /**
     * 특정 날짜의 러닝 기록 조회
     */
    @Override
    public RunningSessionSummaryDTO getDailyRecord(String token, LocalDate date) {
        Long userId = jwtUtil.getUserId(token);

        // 1. 날짜에 해당하는 DailyRunningRecord 조회
        Optional<DailyRunningRecord> dailyRecord = dailyRunningRecordRepository.findByUserIdAndDate(userId, date);
        DailyRunningRecord record = dailyRecord
                .orElseThrow(() -> new RuntimeException("해당 날짜의 DailyRunningRecord가 존재하지 않습니다."));

        // 1-1. 하루 전 DailyRunningRecord와 비교
        LocalDate previousDate = date.minusDays(1);
        Optional<DailyRunningRecord> yesterdayRecord = dailyRunningRecordRepository.findByUserIdAndDate(userId, previousDate);

        Float yDistance = yesterdayRecord.map(DailyRunningRecord::getTotalDistance).orElse(0f);
        Float distance = record.getTotalDistance();

        // 거리 차이 계산
        Float distanceDiff = distance - yDistance;



        List<RunningSessionDTO> sessions = new ArrayList<>();

        Long recordId = record.getRecordId();

        // 2. 해당 기록에 대한 실시간 러닝 데이터 조회
            List<RealTimeRunning> realTimeList = realTimeRunningRepository.findByRecordId(recordId);

            for (RealTimeRunning realTime : realTimeList) {
                Long sessionId = realTime.getRunningSessionId();
                Long routeId = realTime.getRouteId();

                // 3. 러닝 세팅 정보 가져오기
                if (routeId == null) {
                    throw new RuntimeException("routeId is null for RealTimeRunning.sessionId = " + sessionId);
                }

                RunningSettingsResponse settings = null;
                try {
                    settings = runningSettingService.getRunningSettings(routeId);
                } catch (RuntimeException e) {
                    System.out.println("⚠ Route not found for routeId: " + routeId + ", sessionId: " + sessionId);
                    e.printStackTrace();
                    continue;
                }

                // 4. 경로 시간 기반 세부 이력 정보 조회
                List<RunningPathTS> pathList = runningPathTSRepository.findBySessionId(sessionId);

                List<RunningHistoryDetailDTO> detailDTOList = new ArrayList<>();

                for (RunningPathTS path : pathList) {
                    LocationDTO location = LocationDTO.builder()
                            .lat(path.getLatitude())
                            .lon(path.getLongitude())
                            .build();


                    RunningHistoryDetailDTO detail = RunningHistoryDetailDTO.builder()
                            .distance(path.getDistance())
                            .elapsedTime(path.getElapsedTime())
                            .pace((float) path.getPace())
                            .timeStamp(path.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toLocalTime())
                            .location(location)
                            .typeEta(path.getTypeEta() != null ? path.getTypeEta() : 0)
                            .typePace(path.getTypePace() != null ? path.getTypePace() : 0)
                            .typeStop(path.getTypeStop() != null ? path.getTypeStop() : 0)
                            .build();

                    detailDTOList.add(detail);
                }

                // 5. 러닝 이력 DTO 생성
                FeedbackSummary feedback = FeedbackSummary.builder()
                        .main(realTime.getFeedback_main())
                        .advice(realTime.getFeedback_advice())
                        .early_speed_deviation(realTime.getFeedback_earlySpeedDeviation())
                        .build();

                RunningHistoryDTO history = RunningHistoryDTO.builder()
                        .averagePace(realTime.getAvgPace())
                        .comment(realTime.getComment())
                        .completedTime(realTime.getEndTime() != null
                                ? realTime.getEndTime().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
                                : null)
                        .effortLevel(realTime.getEffortLevel())
                        .elapsedTime(realTime.getElapsedTime())
                        .routeId(routeId)
                        .distance(realTime.getDistance())
                        .runningTrackPoint(detailDTOList)
                        .duration(realTime.getDuration())
                        .stopCount(realTime.getStopCount())
                        .focusScore(realTime.getFocusScore())
                        .feedbackSummaryDTO(feedback)
                        .build();

                // 6. 최종 세션 DTO 구성
                RunningSessionDTO sessionDTO = RunningSessionDTO.builder()
                        .runningSettingsResponse(settings)
                        .runningHistoryDTO(history)
                        .build();

                sessions.add(sessionDTO);

            }
        return RunningSessionSummaryDTO.builder()
                .runningSessionDTO(sessions)
                .distanceDiff(distanceDiff)
                .totalDistance(distance)
                .runCount(record.getRunCount())
                .totalRunTime(record.getTotalRunTime())
                .avgSpeed(record.getAvgSpeed())
                .build();
    }



    public RunningHistoryDTO convertToDto(DailyRunningRecord record) {
        return RunningHistoryDTO.builder()
                .elapsedTime(record.getTotalRunTime())
                .distance(record.getTotalDistance())
                .runningTrackPoint(Collections.emptyList())
                .build();
    }



    /**
     * 시작~종료 날짜 구간의 기록 전체 조회 (전체 사용자 기준)
     */
    @Override
    public List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end) {
        Long userId = jwtUtil.getUserId(token);
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 기준으로 날짜 구간에 해당하는 기록 조회
     * TODO: AI 사용 통계 반환
     */
    @Override
    public List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end) {
        Long userId = jwtUtil.getUserId(token);
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
