package com.example.runity.service;

import com.example.runity.DTO.*;
import com.example.runity.domain.*;
//import com.example.runity.domain.RunningPathTS;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RunningPathTSRepository;
import com.example.runity.repository.StatisticsRepository;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RealtimeRunningServiceImpl implements RealtimeRunningService {

    private final RunningPathTSRepository runningPathRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final DailyRunningRecordRepository dailyRunningRecordRepository;
    private final StatisticsRepository statisticsRepository;
    private final WeatherService weatherService;
    private final JwtUtil jwtUtil;

    /**
     * 실시간 러닝 상태 저장
     */
    @Override
    public void saveRunningState(String token, RunningPathDTO dto) {
        Long userId = jwtUtil.getUserId(token);

        String coordinateStr = dto.getCoordinate(); // 예: "37.1234,127.5678"
        String[] parts = coordinateStr.split(",");
        double latitude = Double.parseDouble(parts[0].trim());
        double longitude = Double.parseDouble(parts[1].trim());
        //Instant timestamp = Instant.ofEpochMilli(Long.parseLong(dto.getTimestamp()));


        RunningPathTS path = RunningPathTS.builder()
                .pace(dto.getPace().floatValue())
                .distance(dto.getDistance().floatValue())
                .speed(dto.getSpeed().floatValue())
                .latitude(latitude)
                .longitude(longitude)
                .build();


        runningPathRepository.saveRunningPoint(userId, path);

    }

    @Override
    public void saveRunningStates(Long userId, Long routeId, List<RunningPathDTO> runningPathsDTO) {
        //Long userId = jwtUtil.getUserId(token);

        List<RunningPathTS> paths = runningPathsDTO.stream()
                .map(dto -> {
                    String[] parts = dto.getCoordinate().split(",");
                    double latitude = Double.parseDouble(parts[0].trim());
                    double longitude = Double.parseDouble(parts[1].trim());
                    Instant ts = dto.getTimestamp();

                    return RunningPathTS.builder()
                            .timestamp(ts)
                            .latitude(latitude)
                            .longitude(longitude)
                            .pace(dto.getPace().floatValue())
                            .distance(dto.getDistance().floatValue())
                            .speed(dto.getSpeed().floatValue())
                            .elapsedTime(dto.getElapsedTime())
                            .type(dto.getType())
                            .semiType(dto.getSemiType())
                            .message(dto.getMessage())
                            .build();
                })
                .collect(Collectors.toList());

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
    public void completeRunning(Long userId, RunningCompleteRequest request) {
        //Long userId = jwtUtil.getUserId(token);
        List<RunningPathTS> paths = request.getRunningPaths().stream()
                .map(dto -> {
                    String[] parts = dto.getCoordinate().split(",");
                    double latitude = Double.parseDouble(parts[0].trim());
                    double longitude = Double.parseDouble(parts[1].trim());
                    Instant ts = dto.getTimestamp();

                    return RunningPathTS.builder()
                            .timestamp(ts)
                            .latitude(latitude)
                            .longitude(longitude)
                            .pace(dto.getPace().floatValue())
                            .distance(dto.getDistance().floatValue())
                            .speed(dto.getSpeed().floatValue())
                            .elapsedTime(dto.getElapsedTime())
                            .type(dto.getType())
                            .semiType(dto.getSemiType())
                            .message(dto.getMessage())
                            .build();
                })
                .collect(Collectors.toList());

        if (paths.isEmpty()) return;

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
        float totalPace = 0f;
        float totalSpeed = 0f;
        int count = 0;

        for (RunningPathTS path : paths) {
            totalPace += path.getPace();
            totalSpeed += path.getSpeed();
            count++;
        }

        float avgPace = totalPace / count;
        float avgSpeed = totalSpeed / count;

        // 거리 합계
        float totalDistance = paths.stream()
                .map(RunningPathTS::getDistance)
                .reduce(0f, Float::sum);

        // 러닝 시작/종료 시간 기반 시간 계산
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


        // 4. 종료 시간: 가장 마지막 timestamp
        Instant endTime = paths.stream()
                .map(RunningPathTS::getTimestamp)
                .max(Instant::compareTo)
                .orElse(request.getCompleteTime().atZone(ZoneId.systemDefault()).toInstant());


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
                .avgPace(avgPace)
                .avgSpeed(avgSpeed)
                .distance(totalDistance)
                .runTime(runTime)
                .build();

        // 실시간 경로 저장
        realTimeRunningRepository.save(session);

        Long sessionId = session.getRunningSessionId();
        runningPathRepository.saveAllWithCheck(sessionId, paths);

        System.out.println("sessionId: " + session.getRunningSessionId());

    }



    /**
     * 하루 누적 러닝 기록 저장/업데이트
     */
    @Override
    public void updateDailyRunningRecord(Long userId, LocalDate date) {
        //Long userId = jwtUtil.getUserId(token);

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
            totalRunTime = totalRunTime.plusSeconds(run.getRunTime().toSecondOfDay());
            totalSpeed += run.getAvgSpeed();
            count++;
        }

        float avgSpeed = totalSpeed / count;

        // 3. DailyRunningRecord 저장 또는 업데이트

        // 오늘의 날씨 정보 가져오기
        WeatherDTO weather = weatherService.getWeather("Seoul");

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
                .weather(weather.getWeather())
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
