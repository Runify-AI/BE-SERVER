package com.example.runity.service;

import com.example.runity.DTO.WeatherDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
//import com.example.runity.domain.RunningPathTS;
import com.example.runity.DTO.RunningPathDTO;
import com.example.runity.DTO.RunningCompleteRequest;
import com.example.runity.domain.RunningPathTS;
import com.example.runity.domain.Statistics;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RunningPathTSRepository;
import com.example.runity.repository.StatisticsRepository;
import com.example.runity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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

    /**
     * 러닝 종료 시 전체 데이터 저장
     */
    @Override
    public void completeRunning(String token, RunningCompleteRequest request) {
        Long userId = jwtUtil.getUserId(token);
        List<RunningPathTS> paths = request.getRunningPaths().stream()
                .map(dto -> {
                    String[] parts = dto.getCoordinate().split(",");
                    double latitude = Double.parseDouble(parts[0].trim());
                    double longitude = Double.parseDouble(parts[1].trim());
                    Instant ts = Instant.ofEpochMilli(dto.getTimestamp());

                    return RunningPathTS.builder()
                            .timestamp(ts)
                            .latitude(latitude)
                            .longitude(longitude)
                            .pace(dto.getPace().floatValue())
                            .distance(dto.getDistance().floatValue())
                            .speed(dto.getSpeed().floatValue())
                            .build();
                })
                .collect(Collectors.toList());

        // 1. 실시간 경로 저장
        runningPathRepository.saveAllWithCheck(userId, paths);

        if (paths.isEmpty()) return;

        // 2. 하루 러닝 기록 찾기 (recordId)
        LocalDate runDate = request.getCompleteTime().toLocalDate(); // 날짜만 추출
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, runDate)
                .orElseThrow(() -> new RuntimeException("해당 날짜의 DailyRunningRecord가 존재하지 않습니다."));

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
        RealTimeRunning session = RealTimeRunning.builder()
                .recordId(record.getRecordId()) // 외래키로 연결
                .endTime(endTime)
                .isCompleted(true)
                .avgPace(avgPace)
                .avgSpeed(avgSpeed)
                .distance(totalDistance)
                .runTime(runTime)
                .build();

        realTimeRunningRepository.save(session);
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
            totalRunTime = totalRunTime.plusSeconds(run.getRunTime().toSecondOfDay());
            totalSpeed += run.getAvgSpeed();
            count++;
        }

        float avgSpeed = totalSpeed / count;

        // 3. DailyRunningRecord 저장 또는 업데이트

        // 5. 오늘의 날씨 정보 가져오기
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

        // 4. Statistics 누적값 업데이트
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
