package com.example.runity.service;

import com.example.runity.DTO.runningTS.Statics;
import com.example.runity.DTO.runningTS.RunningPathDTO;
import com.example.runity.DTO.runningTS.RunningCompleteRequest;
import org.springframework.stereotype.Service;
//import com.example.runity.DTO.EvaluationResult;
import java.time.LocalDate;

@Service
public interface RealtimeRunningService {
    // 실시간 경로 저장 (개별 저장)
    void saveRunningState(Long userId, RunningPathDTO dto);

    // 실시간 경로 저장 (토큰 + 경로 기반)
    void saveRunningStates(String token, Long routeId, RunningPathDTO dto);

    // 러닝 종료 시 전체 데이터 저장 및 세션 종료
    Long completeRunning(String token, RunningCompleteRequest request);

    // 하루 누적 러닝 기록 집계 및 저장
    void updateDailyRunningRecord(String token, LocalDate date);

    // 러닝 통계 분석 (AI 호출)
    Statics analyzeRunningStatistics(String token, Long sessionId);
}
