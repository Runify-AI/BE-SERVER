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
        //Long userId = jwtUtil.getUserId(token);

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

        if (dto == null) return; // 방어 코드

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

        // 2) 비어 있으면 종료
        if (paths.isEmpty()) {
            return;
        }

        // 2. 하루 러닝 기록 찾기 (recordId)
        LocalDate runDate = LocalDate.now(); // 오늘 날짜
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, runDate)
                .orElseGet(() -> {
                    // 새 기록 생성
                    DailyRunningRecord newRecord = DailyRunningRecord.builder()
                            .userId(userId)
                            .totalDistance(0f)
                            .totalRunTime(LocalTime.of(0, 0))
                            .date(runDate)
                            .runCount(1)
                            .avgSpeed(0f)
                            .weather("Unknown")         // nullable, 임시값
                            .temperature(0f)            // nullable, 임시값
                            .humidity(0f)               // nullable, 임시값
                            .build();

                    return dailyRunningRecordRepository.save(newRecord);
                });

        // routeid로 러닝 세션 조회 또는 생성
        RealTimeRunning session = realTimeRunningRepository.findByRouteIdAndIsCompleted(routeId, false)
                .orElseGet(() -> {
                    RealTimeRunning newSession = RealTimeRunning.builder()
                            .recordId(record.getRecordId())
                            .routeId(routeId)
                            .isCompleted(false)               // 기본 false
                            .build();
                    return realTimeRunningRepository.save(newSession);
                });

        // 실시간 경로 저장
        Long sessionId = session.getRunningSessionId();
        runningPathRepository.saveAllWithCheck(sessionId, paths);

        System.out.println("sessionId: " + session.getRunningSessionId());
    }




    /**
     * 러닝 종료 시 전체 데이터 저장
     */
    @Override
    public Long completeRunning(String token, RunningCompleteRequest request) {
        Long userId = jwtUtil.getUserId(token);

        List<RunningPathTS> paths = request.getRunningPaths().stream()
                .map(dto -> {
                    Instant ts = dto.getTimestamp();

                    return RunningPathTS.builder()
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
                })
                .collect(Collectors.toList());

        if (paths.isEmpty()) return -1L;

        // 2. 하루 러닝 기록 찾기 (recordId)
        LocalDate runDate = request.getCompleteTime().toLocalDate(); // 날짜만 추출
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, runDate)
                .orElseGet(() -> {
                    // 새 기록 생성
                    DailyRunningRecord newRecord = DailyRunningRecord.builder()
                            .userId(userId)
                            .totalDistance(0f)
                            .totalRunTime(LocalTime.of(0, 0))
                            .date(runDate)
                            .runCount(1)
                            .avgSpeed(0f)
                            .weather("Unknown")         // nullable, 임시값
                            .temperature(0f)            // nullable, 임시값
                            .humidity(0f)               // nullable, 임시값
                            .build();

                    return dailyRunningRecordRepository.save(newRecord);
                });

        // 3. 통계 계산
        //float totalPace = 0f;
        // 3-1. 페이스, 속도
        //float avgPace = count > 0 ? totalPace / count : 0f;
        /*
        // 3-3. 러닝 시작/종료 시간 기반 시간 계산
        Instant start = paths.stream()
                .map(RunningPathTS::getTimestamp)
                .min(Instant::compareTo)
                .orElse(request.getCompleteTime().atZone(ZoneId.systemDefault()).toInstant());

        Instant end = paths.stream()
                .map(RunningPathTS::getTimestamp)
                .max(Instant::compareTo)
                .orElse(request.getCompleteTime().atZone(ZoneId.systemDefault()).toInstant());


        long seconds = Duration.between(start, end).getSeconds();
        LocalTime runTime = LocalTime.ofSecondOfDay(seconds);
        */

        // 서버에 필요한 통계 계산
        float totalSpeed = 0f;
        int count = 0;

        for (RunningPathTS path : paths) {
            //totalPace += path.getPace();
            totalSpeed += path.getSpeed();
            count++;
        }

        float avgSpeed = count > 0 ? totalSpeed / count : 0f;

        // 하루 러닝 기록에 합계
        // 기존 값 누적 반영
        //float updatedTotalDistance = record.getTotalDistance() + totalDistance;
        float dailyAvgSpeed = record.getAvgSpeed() > 0 ? (totalSpeed + record.getAvgSpeed()) / (count + 1) : 0f;

        DailyRunningRecord updated = record.toBuilder()
                .avgSpeed(dailyAvgSpeed)
                //.totalDistance(updatedTotalDistance)
                //.totalRunTime(runTime)
                .build();
        dailyRunningRecordRepository.save(updated);


        // 4. 종료 시간
        LocalDateTime endTime = request.getCompleteTime();

        // 경과 시간
        LocalTime elapsedTime = paths.get(paths.size() - 1).getElapsedTime();

        /*
        // 4-1. 평균 정지 시간 계산
        float totalStopTime = 0f;

        for (int i = 1; i < paths.size(); i++) {
            RunningPathTS prev = paths.get(i - 1);
            RunningPathTS curr = paths.get(i);

            boolean sameLocation = prev.getLatitude() == curr.getLatitude()
                    && prev.getLongitude() == curr.getLongitude();

            if (sameLocation) {
                long stopSeconds = Duration.between(prev.getTimestamp(), curr.getTimestamp()).getSeconds();
                totalStopTime += stopSeconds;
            }
        }

        float avgStopTime = (seconds > 0) ? totalStopTime / seconds : 0f;

         */

        // 5. 실시간 러닝 세션 저장
        Long routeId = request.getRouteId();
        // routeId로 '미완료된' 러닝 세션 조회 또는 생성
        RealTimeRunning session = realTimeRunningRepository.findByRouteIdAndIsCompleted(routeId, false)
                .orElseGet(() -> {
                    RealTimeRunning newSession = RealTimeRunning.builder()
                            .recordId(record.getRecordId())
                            .routeId(routeId)
                            .isCompleted(false) // 처음에는 false로 생성
                            .build();
                    return realTimeRunningRepository.save(newSession);
                });
        // 기존 세션 객체를 갱신: toBuilder()를 사용해서 ID 유지
        session = session.toBuilder()
                .comment(request.getComment())
                .effortLevel(request.getEffortLevel())
                .endTime(endTime)
                .isCompleted(true)
                .avgSpeed(avgSpeed)
                .elapsedTime(elapsedTime)
                //.avgPace(avgPace)
                //.distance(totalDistance)
                //.runTime(runTime)
                //.avgStopTime(avgStopTime)
                .build();

        // 실시간 경로 저장
        realTimeRunningRepository.save(session);

        Long sessionId = session.getRunningSessionId();
        runningPathRepository.saveAllWithCheck(sessionId, paths);

        System.out.println("sessionId: " + session.getRunningSessionId());

        return sessionId;
    }

    /**
     * 통계 AI 호출
     */
    @Override
    public Statics analyzeRunningStatistics(String token, Long sessionId) {
        Long userId = jwtUtil.getUserId(token);

        // [1] sessionId로 RunningPathTS 전체 조회
        List<RunningPathTS> pathList = runningPathRepository.findBySessionId(sessionId);

        if (pathList.isEmpty()) {
            throw new RuntimeException("러닝 경로 데이터가 존재하지 않습니다.");
        }

        // [1-2] RunningPathTS → StaticsHistoryDTO 리스트로 변환
        List<StaticsHistoryRequestDTO.StaticsHistoryDTO> historyList = pathList.stream()
                .map(p -> StaticsHistoryRequestDTO.StaticsHistoryDTO.builder()
                        .distance(p.getDistance())
                        .elapsedTime(p.getElapsedTime() != null ? p.getElapsedTime().toSecondOfDay() : 0)
                        .pace(p.getPace())
                        .build())
                .collect(Collectors.toList());

        // [1-3] DTO 감싸기
        StaticsHistoryRequestDTO history = StaticsHistoryRequestDTO.builder()
                .history(historyList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(history);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 또는 로그 처리
        }

        // [2] AI 분석기 호출
        FeedbackStaticsResponse result = mockAIAnalyze(history);

        // 3. DB 저장
        RealTimeRunning session = realTimeRunningRepository.findByRunningSessionId(sessionId);
        if (session == null) {
            throw new RuntimeException("해당 sessionId를 가진 러닝 세션을 찾을 수 없습니다.");
        }

        Statics statics = result.getStatics();

        if (statics == null) {
            throw new IllegalStateException("AI 분석 결과가 null입니다. (Statics 객체 없음)");
        }

        FeedbackSummary feedback = statics.getFeedback_summary();
        String main = feedback != null ? feedback.getMain() : null;
        String advice = feedback != null ? feedback.getAdvice() : null;
        double earlySpeedDeviation = feedback != null ? feedback.getEarly_speed_deviation() : 0.0;

        session = session.toBuilder()
                .distance(statics.getDistance())
                .duration((float) statics.getDuration())
                .avgPace(statics.getAveragePace())
                .stopCount(statics.getStopCount())
                .feedback_main(main)
                .feedback_advice(advice)
                .feedback_earlySpeedDeviation(earlySpeedDeviation)
                .focusScore(statics.getFocus_score())
                .build();

        realTimeRunningRepository.save(session);  // 수정된 session 저장

        return statics;
    }
    private FeedbackStaticsResponse mockAIAnalyze(StaticsHistoryRequestDTO dto) {
         /*
        return RunningFeedbackDTO.builder()
                .distance(3.21f)
                .duration(25)
                .avgPace(7.6f)
                .stopCount(3)
                .focusScore(78)
                .feedbackSummary(FeedbackSummaryDTO.builder()
                        .main("초반과 후반 속도 차이가 있어요.")
                        .advice("다음엔 초반 속도를 더 조절해보세요.")
                        .earlySpeedDeviation(1.2f)
                        .build())
                .build();

          */

        // /*
        System.out.println("[통계 AI 요청 시작]");
        System.out.println("Payload: " + dto);
        return aiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/statics/")
                        .build())
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
                                .doOnNext(json -> {
                                    System.out.println("[AI 응답 원문]");
                                    System.out.println(json);
                                })
                )
                .bodyToMono(FeedbackStaticsResponse.class)
                .block();

         // */

    }


    /**
     * 하루 누적 러닝 기록 저장/업데이트
     */
    @Override
    public void updateDailyRunningRecord(String token, LocalDate date) {
        Long userId = jwtUtil.getUserId(token);

        // 1. 오늘 날짜의 러닝 기록 모두 조회
        List<RealTimeRunning> todayRuns = realTimeRunningRepository.findByUserIdAndDate(userId, date);

        if (todayRuns.isEmpty()) return;

        // 2. 집계값 계산
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
                count++;
            }
            count++;

            routeRepository.findById(run.getRouteId()).ifPresent(route -> {
                route.setCompleted(true);
                routeRepository.save(route);
            });
        }

        float avgSpeed = (count > 0) ? totalSpeed / count : 0f;

        // 3. DailyRunningRecord 저장 또는 업데이트

        // 오늘의 날씨 정보 가져오기
        WeatherDTO weather = weatherService.getWeather("Daegu");

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

        // Statistics 누적값 업데이트
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
