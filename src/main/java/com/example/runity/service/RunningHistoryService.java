package com.example.runity.service;

import com.example.runity.DTO.history.RunningHistoryDTO;
import com.example.runity.DTO.history.RunningSessionSummaryDTO;
import com.example.runity.domain.DailyRunningRecord;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface RunningHistoryService {

    // 월별 일자별 거리 조회
    List<Float> getMonthlyDistances(String token, YearMonth yearMonth);

    // 특정 날짜의 러닝 기록 요약 조회
    RunningSessionSummaryDTO getDailyRecord(String token, LocalDate date);

    // 엔티티를 DTO로 변환
    RunningHistoryDTO convertToDto(DailyRunningRecord record);

    // 기간 내 러닝 기록 조회
    List<RunningHistoryDTO> getPeriodRecord(String token, LocalDate start, LocalDate end);

    // 사용자 기준 러닝 기록 조회
    List<RunningHistoryDTO> getUserRunningHistories(String token, LocalDate start, LocalDate end);
}
