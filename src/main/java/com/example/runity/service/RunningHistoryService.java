package com.example.runity.service;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.DTO.RunningSessionDTO;
import com.example.runity.DTO.RunningSessionSummaryDTO;
import com.example.runity.domain.DailyRunningRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public interface RunningHistoryService {
    RunningSessionSummaryDTO getDailyRecord(Long userId, LocalDate date);
    List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end);
    RunningHistoryDTO convertToDto(DailyRunningRecord record);
    List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end);
    List<Float> getMonthlyDistances(Long userId, YearMonth yearMonth);
}
