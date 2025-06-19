package com.example.runity.service;

import com.example.runity.DTO.RunningPathDTO;
import com.example.runity.DTO.RunningCompleteRequest;
import org.springframework.stereotype.Service;
//import com.example.runity.DTO.EvaluationResult;
import java.time.LocalDate;
import java.util.List;

@Service
public interface RealtimeRunningService {

    void saveRunningState(Long userId, RunningPathDTO dto);  // 실시간 러닝 중 위치/속도 등 기록 저장

    void saveRunningStates(Long userId, Long routeId, List<RunningPathDTO> dto);

    //void completeRunning(String token, RunningCompleteRequest request);  // 러닝 완료 시 전체 데이터 저장
    void completeRunning(Long userId, RunningCompleteRequest request);  // 러닝 완료 시 전체 데이터 저장

    void updateDailyRunningRecord(Long userId, LocalDate date);  // 하루 요약 러닝 기록 저장

    //EvaluationResult evaluateRunningPerformance(Double pace, int time, double distance, int stopCount);  // 러닝 평가
}
