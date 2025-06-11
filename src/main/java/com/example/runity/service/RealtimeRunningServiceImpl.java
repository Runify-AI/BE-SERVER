package com.example.runity.service;

import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
//import com.example.runity.domain.RunningPathTS;
import com.example.runity.DTO.RunningPathDTO;
import com.example.runity.DTO.RunningCompleteRequest;
import com.example.runity.domain.RunningPathTS;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RunningPathTSRepository;
import com.example.runity.service.RealtimeRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealtimeRunningServiceImpl implements RealtimeRunningService {

    private final RunningPathTSRepository runningPathRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final DailyRunningRecordRepository dailyRunningRecordRepository;

    /**
     * 실시간 러닝 상태 저장
     */
    @Override
    public void saveRunningState(RunningPathDTO dto) {

        String coordinateStr = dto.getCoordinate(); // "37.1234,127.5678"
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


        runningPathRepository.saveRunningPoint(dto.getUserId(), path);

    }

    /**
     * 러닝 종료 시 전체 데이터 저장
     */
    @Override
    public void completeRunning(RunningCompleteRequest request) {
        // 실시간 경로 저장
        /*
        List<RunningPathTS> paths = request.getRunningPaths().stream()
                .map(dto -> RunningPathTS.builder()
                        .timestamp(dto.getTimestamp())
                        .pace(dto.getPace().floatValue())
                        .distance(dto.getDistance().floatValue())
                        .speed(dto.getSpeed().floatValue())
                        .latitude(dto.getCoordinate().getLatitude())
                        .longitude(dto.getCoordinate().getLongitude())
                        .build())
                .collect(Collectors.toList());
        runningPathRepository.saveAll(paths);
         */

        // 피드백 저장
        RealTimeRunning session = RealTimeRunning.builder()
                .endTime(request.getCompleteTime())
                .effortLevel(request.getEffortLevel())
                .comment(request.getComment())
                .isCompleted(true)
                .build();
        realTimeRunningRepository.save(session);
    }

    /**
     * 하루 누적 러닝 기록 저장/업데이트
     */
    @Override
    public void updateDailyRunningRecord(Long userId, LocalDate date) {
        DailyRunningRecord record = dailyRunningRecordRepository.findByUserIdAndDate(userId, date)
                .orElse(DailyRunningRecord.builder()
                        .userId(userId)
                        .date(date)
                        .totalDistance(0f)
                        .totalRunTime(LocalTime.of(0, 0))
                        .runCount(0)
                        .avgSpeed(0f)
                        .build());

        // 예시: 거리 3km, 시간 18분, 속도 10km/h 로 가정
        float newDistance = 3.0f;
        LocalTime newRunTime = LocalTime.of(0, 18);
        float newAvgSpeed = 10.0f;

        record = record.toBuilder()
                .totalDistance(record.getTotalDistance() + newDistance)
                .totalRunTime(record.getTotalRunTime().plusMinutes(newRunTime.getMinute()))
                .runCount(record.getRunCount() + 1)
                .avgSpeed((record.getAvgSpeed() * record.getRunCount() + newAvgSpeed) / (record.getRunCount() + 1))
                .build();

        dailyRunningRecordRepository.save(record);
    }
}
