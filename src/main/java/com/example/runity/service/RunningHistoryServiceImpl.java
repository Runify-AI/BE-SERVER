package com.example.runity.service;

import com.example.runity.DTO.RealTimeRunningDTO;
import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.DTO.RunningHistoryDetailDTO;
import com.example.runity.DTO.WeatherDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.domain.RealTimeRunning;
import com.example.runity.domain.RunningPathTS;
import com.example.runity.repository.DailyRunningRecordRepository;
import com.example.runity.repository.RealTimeRunningRepository;
import com.example.runity.repository.RunningPathTSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningHistoryServiceImpl implements RunningHistoryService {

    private final DailyRunningRecordRepository dailyRunningRecordRepository;
    private final RealTimeRunningRepository realTimeRunningRepository;
    private final RunningPathTSRepository runningPathTSRepository;

    /**
     * 특정 날짜의 러닝 기록 조회
     */
    @Override
    public RunningHistoryDetailDTO getDailyRecord(Long userId, LocalDate date) {
        // 1. DailyRunningRecord 조회
        DailyRunningRecord record = dailyRunningRecordRepository
                .findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new RuntimeException("해당 날짜의 기록이 없습니다."));

        // 2. RealTimeRunning 리스트 조회
        List<RealTimeRunning> sessions = realTimeRunningRepository.findByRecordId(record.getRecordId());

        // 3. 각 세션마다 RunningPathTS 리스트 조회 후 DTO 구성
        List<RealTimeRunningDTO> runningSessionDTOs = sessions.stream()
                .map(session -> {
                    List<RunningPathTS> pathList = runningPathTSRepository.findByRecordId(session.getRecordId());

                    return RealTimeRunningDTO.builder()
                            .endTime(LocalDateTime.ofInstant(session.getEndTime(), ZoneId.systemDefault()))
                            .isCompleted(session.getIsCompleted())
                            .giveUpReason(session.getGiveUpReason())
                            .effortLevel(session.getEffortLevel())
                            .avgPace(session.getAvgPace())
                            .avgSpeed(session.getAvgSpeed())
                            .pathList(pathList)
                            .build();
                }).collect(Collectors.toList());

        // 4. 최종 DTO 구성
        return RunningHistoryDetailDTO.builder()
                .recordId(record.getRecordId())
                .userId(record.getUserId())
                .totalDistance(record.getTotalDistance())
                .totalRunTime(record.getTotalRunTime())
                .date(record.getDate())
                .runCount(record.getRunCount())
                .avgSpeed(record.getAvgSpeed())
                .runningSessions(runningSessionDTOs)
                .build();
    }



    /**
     * 시작~종료 날짜 구간의 기록 전체 조회 (전체 사용자 기준)
     */
    @Override
    public List<RunningHistoryDTO> getPeriodRecord(Long userId, LocalDate start, LocalDate end) {
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 하나의 DailyRunningRecord → DTO 변환
     */
    @Override
    public RunningHistoryDTO convertToDto(DailyRunningRecord record) {
        return RunningHistoryDTO.builder()
                .userId(record.getUserId())
                .date(record.getDate())
                .totalDistance(record.getTotalDistance())
                .totalRunTime(record.getTotalRunTime())
                .avgSpeed(record.getAvgSpeed())
                .runCount(record.getRunCount())
                .build();
    }

    /**
     * 사용자 기준으로 날짜 구간에 해당하는 기록 조회
     */
    @Override
    public List<RunningHistoryDTO> getUserRunningHistories(Long userId, LocalDate start, LocalDate end) {
        return dailyRunningRecordRepository.findByUserIdAndDateBetween(userId, start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
