package com.example.runity.service;

import com.example.runity.DTO.*;
import com.example.runity.DTO.runningTS.*;
import com.example.runity.domain.*;
//import com.example.runity.domain.RunningPathTS;
import com.example.runity.repository.*;
import com.example.runity.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealtimeRunningServiceImpl implements RealtimeRunningService {

    private final RunningPathTSRepository runningPathRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final DailyRunningRecordRepository dailyRunningRecordRepository;
    private final StatisticsRepository statisticsRepository;
    private final WeatherService weatherService;
    private final RunningHistoryService runningHistoryService;
    private final RouteRepository routeRepository;
    private final JwtUtil jwtUtil;

    private final WebClient aiWebClient;

    /**
     * 실시간 러닝 상태 저장
     */
    @Override
    public void saveRunningState(Long userId, RunningPathDTO dto) {
        RunningPathTS path = RunningPathTS.builder()
                .pace(dto.getPace().floatValue())
                .distance(dto.getDistance().floatValue())
                .speed(dto.getSpeed().floatValue())
                .latitude(dto.getCoordinate().getLatitude())
                .longitude(dto.getCoordinate().getLongitude())
                .elapsedTime(dto.getElapsedTime())
                .typeEta(dto.getTypeEta())
                .typePace(dto.getTypePace())
                .typeStop(dto.getTypeStop())
                .build();

        runningPathRepository.saveRunningPoint(userId, path);
    }

    @Override
    public void saveRunningStates(String token, Long routeId, RunningPathDTO dto) {
        Long userId = jwtUtil.getUserId(token);
        if (dto == null) return;

        Instant ts = dto.getTimestamp();

        RunningPathTS path = RunningPathTS.builder()
                .timestamp(ts)
                .latitude(dto.getCoordinate().getLatitude())
                .longitude(dto.getCoordinate().getLongitude())
                .pace(dto.getPace().floatValue())
                .distance(dto.getDistance().floatValue())
                .speed(dto.getSpeed().floatValue())
                .elapsedTime(dto.getElapsedTime())
                .typeEta(dto.getTypeEta())
                .typePace(dto.getTypePace())
                .typeStop(dto.getTypeStop())
                .build();

        List<RunningPathTS> paths = List.of(path);

        if (paths.isEmpty()) return;

        // 하루 러닝 기록 조회 또는 생성
        LocalDate runDate = LocalDate.now();
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, runDate)
                .orElseGet(() -> dailyRunningRecordRepository.save(
                        DailyRunningRecord.builder()
                                .userId(userId)
                                .totalDistance(0f)
                                .totalRunTime(LocalTime.of(0, 0))
                                .date(runDate)
                                .runCount(1)
                                .avgSpeed(0f)
                                .weather("Unknown")
                                .temperature(0f)
                                .humidity(0f)
                                .build()
                ));

        // 미완료된 세션 조회 또는 생성
        RealTimeRunning session = realTimeRunningRepository.findByRouteIdAndIsCompleted(routeId, false)
                .orElseGet(() -> realTimeRunningRepository.save(
                        RealTimeRunning.builder()
                                .recordId(record.getRecordId())
                                .routeId(routeId)
                                .isCompleted(false)
                                .build()
                ));

        runningPathRepository.saveAllWithCheck(session.getRunningSessionId(), paths);

        System.out.println("sessionId: " + session.getRunningSessionId());
    }

    /**
     * 러닝 종료 시 전체 데이터 저장
     */
    @Override
    public Long completeRunning(String token, RunningCompleteRequest request) {
        Long userId = jwtUtil.getUserId(token);

        List<RunningPathTS> paths = request.getRunningPaths().stream()
                .map(dto -> RunningPathTS.builder()
                        .timestamp(dto.getTimestamp())
                        .latitude(dto.getCoordinate().getLatitude())
                        .longitude(dto.getCoordinate().getLongitude())
                        .pace(dto.getPace().floatValue())
                        .distance(dto.getDistance().floatValue())
                        .speed(dto.getSpeed().floatValue())
                        .elapsedTime(dto.getElapsedTime())
                        .typeEta(dto.getTypeEta())
                        .typePace(dto.getTypePace())
                        .typeStop(dto.getTypeStop())
                        .build())
                .collect(Collectors.toList());

        if (paths.isEmpty()) return -1L;

        // 러닝 기록 조회 또는 생성
        LocalDate runDate = request.getCompleteTime().toLocalDate();
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, runDate)
                .orElseGet(() -> dailyRunningRecordRepository.save(
                        DailyRunningRecord.builder()
                                .userId(userId)
                                .totalDistance(0f)
                                .totalRunTime(LocalTime.of(0, 0))
                                .date(runDate)
                                .runCount(1)
                                .avgSpeed(0f)
                                .weather("Unknown")
                                .temperature(0f)
                                .humidity(0f)
                                .build()
                ));

        // 통계 계산
        float totalSpeed = 0f;
        int count = 0;
        for (RunningPathTS path : paths) {
            totalSpeed += path.getSpeed();
            count++;
        }

        float avgSpeed = count > 0 ? totalSpeed / count : 0f;
        float dailyAvgSpeed = record.getAvgSpeed() > 0 ? (totalSpeed + record.getAvgSpeed()) / (count + 1) : 0f;

        dailyRunningRecordRepository.save(record.toBuilder()
                .avgSpeed(dailyAvgSpeed)
                .build());

        LocalDateTime endTime = request.getCompleteTime();
        LocalTime elapsedTime = paths.get(paths.size() - 1).getElapsedTime();

        // 세션 조회 또는 생성
        Long routeId = request.getRouteId();
        RealTimeRunning session = realTimeRunningRepository.findByRouteIdAndIsCompleted(routeId, false)
                .orElseGet(() -> realTimeRunningRepository.save(
                        RealTimeRunning.builder()
                                .recordId(record.getRecordId())
                                .routeId(routeId)
                                .isCompleted(false)
                                .build()
                ));

        // 세션 업데이트
        session = session.toBuilder()
                .comment(request.getComment())
                .effortLevel(request.getEffortLevel())
                .endTime(endTime)
                .isCompleted(true)
                .avgSpeed(avgSpeed)
                .elapsedTime(elapsedTime)
                .build();

        realTimeRunningRepository.save(session);
        runningPathRepository.saveAllWithCheck(session.getRunningSessionId(), paths);

        System.out.println("sessionId: " + session.getRunningSessionId());

        return session.getRunningSessionId();
    }

    /**
     * 통계 AI 호출
     */
    @Override
    public Statics analyzeRunningStatistics(String token, Long sessionId) {
        Long userId = jwtUtil.getUserId(token);

        // 러닝 경로 조회
        List<RunningPathTS> pathList = runningPathRepository.findBySessionId(sessionId);
        if (pathList.isEmpty()) throw new RuntimeException("러닝 경로 데이터가 존재하지 않습니다.");

        // 경로 → 히스토리 DTO 변환
        List<StaticsHistoryRequestDTO.StaticsHistoryDTO> historyList = pathList.stream()
                .map(p -> StaticsHistoryRequestDTO.StaticsHistoryDTO.builder()
                        .distance(p.getDistance())
                        .elapsedTime(p.getElapsedTime() != null ? p.getElapsedTime().toSecondOfDay() : 0)
                        .pace(p.getPace())
                        .build())
                .collect(Collectors.toList());

        StaticsHistoryRequestDTO history = StaticsHistoryRequestDTO.builder()
                .history(historyList)
                .build();

        try {
            System.out.println(new ObjectMapper().writeValueAsString(history));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // AI 호출
        FeedbackStaticsResponse result = mockAIAnalyze(history);

        // 결과 저장
        RealTimeRunning session = realTimeRunningRepository.findByRunningSessionId(sessionId);
        if (session == null) throw new RuntimeException("해당 sessionId를 가진 러닝 세션을 찾을 수 없습니다.");

        Statics statics = result.getStatics();
        if (statics == null) throw new IllegalStateException("AI 분석 결과가 null입니다.");

        FeedbackSummary feedback = statics.getFeedback_summary();
        session = session.toBuilder()
                .distance(statics.getDistance())
                .duration((float) statics.getDuration())
                .avgPace(statics.getAveragePace())
                .stopCount(statics.getStopCount())
                .feedback_main(feedback != null ? feedback.getMain() : null)
                .feedback_advice(feedback != null ? feedback.getAdvice() : null)
                .feedback_earlySpeedDeviation(feedback != null ? feedback.getEarly_speed_deviation() : 0.0)
                .focusScore(statics.getFocus_score())
                .build();

        realTimeRunningRepository.save(session);
        return statics;
    }

    private FeedbackStaticsResponse mockAIAnalyze(StaticsHistoryRequestDTO dto) {
        System.out.println("[통계 AI 요청 시작]");
        System.out.println("Payload: " + dto);

        return aiWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/statics/").build())
                .bodyValue(dto)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(errorBody -> {
                                    System.out.println("[AI 요청 실패]");
                                    System.out.println("상태 코드: " + response.statusCode());
                                    System.out.println("에러 본문: " + errorBody);
                                    return new RuntimeException("AI 서버 오류: " + response.statusCode() + " - " + errorBody);
                                })
                )
                .bodyToMono(FeedbackStaticsResponse.class)
                .block();
    }

    /**
     * 하루 누적 러닝 기록 저장/업데이트
     */
    @Override
    public void updateDailyRunningRecord(String token, LocalDate date) {
        Long userId = jwtUtil.getUserId(token);

        // 해당 날짜의 세션 조회
        List<RealTimeRunning> todayRuns = realTimeRunningRepository.findByUserIdAndDate(userId, date);
        if (todayRuns.isEmpty()) return;

        // 집계값 계산
        float totalDistance = 0f;
        LocalTime totalRunTime = LocalTime.of(0, 0);
        float totalSpeed = 0f;
        int count = 0;

        for (RealTimeRunning run : todayRuns) {
            totalDistance += run.getDistance();
            if (run.getElapsedTime() != null) {
                totalRunTime = totalRunTime.plusSeconds(run.getElapsedTime().toSecondOfDay());
            }
            if (run.getAvgSpeed() != null) {
                totalSpeed += run.getAvgSpeed();
            }
            count++;

            // 해당 루트 완료 처리
            routeRepository.findById(run.getRouteId()).ifPresent(route -> {
                route.setCompleted(true);
                routeRepository.save(route);
            });
        }

        float avgSpeed = (count > 0) ? totalSpeed / count : 0f;

        // 날씨 정보 가져오기
        WeatherDTO weather = weatherService.getWeather("Daegu");

        // 기록 저장 또는 업데이트
        DailyRunningRecord dailyRecord = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, date)
                .orElse(DailyRunningRecord.builder()
                        .userId(userId)
                        .date(date)
                        .totalDistance(0f)
                        .totalRunTime(LocalTime.of(0, 0))
                        .runCount(0)
                        .avgSpeed(0f)
                        .build());

        dailyRecord = dailyRecord.toBuilder()
                .totalDistance(totalDistance)
                .totalRunTime(totalRunTime)
                .runCount(count)
                .avgSpeed(avgSpeed)
                .weather(weather.getCondition())
                .temperature(weather.getTemperature().floatValue())
                .humidity(weather.getHumidity().floatValue())
                .build();

        dailyRunningRecordRepository.save(dailyRecord);

        // 전체 통계 업데이트
        Statistics statistics = statisticsRepository
                .findByUserId(userId)
                .orElse(Statistics.builder()
                        .userId(userId)
                        .totalDistance(0f)
                        .totalRunTime(LocalTime.of(0, 0))
                        .build());

        statistics = statistics.toBuilder()
                .totalDistance(statistics.getTotalDistance() + totalDistance)
                .totalRunTime(statistics.getTotalRunTime().plusSeconds(totalRunTime.toSecondOfDay()))
                .build();

        statisticsRepository.save(statistics);
    }
}
