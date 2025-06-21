package com.example.runity.service;

import com.example.runity.DTO.history.RunningHistoryDTO;
import com.example.runity.DTO.history.RunningSessionSummaryDTO;
import com.example.runity.domain.DailyRunningRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public interface RunningHistoryService {
    RunningSessionSummaryDTO getDailyRecord(String token, LocalDate date);
    List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end);
    RunningHistoryDTO convertToDto(DailyRunningRecord record);
    List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end);
    List<Float> getMonthlyDistances(String token, YearMonth yearMonth);
}
