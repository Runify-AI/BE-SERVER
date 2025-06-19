package com.example.runity.service;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.DTO.RunningSessionDTO;
import com.example.runity.domain.DailyRunningRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RunningHistoryService {
    List<RunningSessionDTO> getDailyRecord(Long userId, LocalDate date);
    List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end);
    RunningHistoryDTO convertToDto(DailyRunningRecord record);
    List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end);
}
