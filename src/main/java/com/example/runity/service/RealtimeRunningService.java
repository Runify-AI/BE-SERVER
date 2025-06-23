package com.example.runity.service;

import com.example.runity.DTO.runningTS.Statics;
import com.example.runity.DTO.runningTS.RunningPathDTO;
import com.example.runity.DTO.runningTS.RunningCompleteRequest;
import org.springframework.stereotype.Service;
//import com.example.runity.DTO.EvaluationResult;
import java.time.LocalDate;

@Service
public interface RealtimeRunningService {

    void saveRunningState(Long userId, RunningPathDTO dto);  // 실시간 러닝 중 위치/속도 등 기록 저장

    void saveRunningStates(String token, Long routeId, RunningPathDTO dto);

    Long completeRunning(String token, RunningCompleteRequest request);  // 러닝 완료 시 전체 데이터 저장

    void updateDailyRunningRecord(String token, LocalDate date);  // 하루 요약 러닝 기록 저장

    Statics analyzeRunningStatistics(String token, Long sessionId);
}
