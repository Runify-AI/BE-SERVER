package com.example.runity.service;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.domain.DailyRunningRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RunningHistoryService {
    DailyRunningRecord getDailyRecord(LocalDate date);
    List<RunningHistoryDTO> getPeriodRecord(LocalDate start, LocalDate end);
    RunningHistoryDTO convertToDto(DailyRunningRecord record);
    List<RunningHistoryDTO> getUserRunningHistories(Long userId, LocalDate start, LocalDate end);
}
