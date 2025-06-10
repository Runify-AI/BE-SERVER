package com.example.runity.service;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.repository.DailyRunningRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningHistoryServiceImpl implements RunningHistoryService {

    private final DailyRunningRecordRepository dailyRunningRecordRepository;

    /**
     * 특정 날짜의 러닝 기록 조회
     */
    @Override
    public DailyRunningRecord getDailyRecord(LocalDate date) {
        // 단일 기록 조회, userId 조건 없는 경우
        return dailyRunningRecordRepository.findByDate(date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜에 대한 러닝 기록이 존재하지 않습니다."));
    }

    /**
     * 시작~종료 날짜 구간의 기록 전체 조회 (전체 사용자 기준)
     */
    @Override
    public List<RunningHistoryDTO> getPeriodRecord(LocalDate start, LocalDate end) {
        return dailyRunningRecordRepository.findAllByDateBetween(start, end).stream()
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
