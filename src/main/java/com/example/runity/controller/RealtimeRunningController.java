package com.example.runity.controller;

import com.example.runity.DTO.RunningCompleteRequest;
import com.example.runity.DTO.RunningPathDTO;
//import com.example.runity.DTO.EvaluationResult;
import com.example.runity.service.RealtimeRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/running")
public class RealtimeRunningController {

    private final RealtimeRunningService realtimeRunningService;

    // 5분 주기로 사용자 러닝 정보를 저장
    @PostMapping("/state")
    public ResponseEntity<Void> saveRunningState(@RequestHeader("Authorization") String token,
                                                 @RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningState(token, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> completeRunning(@RequestHeader("Authorization") String token,
                                                @RequestBody RunningCompleteRequest request) {
        realtimeRunningService.completeRunning(token, request);
        realtimeRunningService.updateDailyRunningRecord(token, LocalDate.now());
        return ResponseEntity.ok().build();
    }

    // TODO: 통계 AI와 연결
    @PostMapping("/evaluate")
    public ResponseEntity<Void> evaluateRunning(
            @RequestParam Double pace,
            @RequestParam int time,
            @RequestParam double distance,
            @RequestParam int stopCount) {
        //return ResponseEntity.ok(
        //        realtimeRunningService.evaluateRunningPerformance(pace, time, distance, stopCount)
        //);
        return ResponseEntity.ok().build();
    }
}
