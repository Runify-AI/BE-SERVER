package com.example.runity.controller;

import com.example.runity.DTO.RunningCompleteRequest;
import com.example.runity.DTO.RunningPathDTO;
import com.example.runity.service.RealtimeRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*") // 또는 정확한 origin만
@RestController
@RequiredArgsConstructor
@RequestMapping("/running")
public class RealtimeRunningController {

    private final RealtimeRunningService realtimeRunningService;

    // 5분 주기로 사용자 러닝 정보를 저장
    @PostMapping("/state")
    public ResponseEntity<Void> saveRunningState(@RequestHeader("userId") Long userId,
                                                 @RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningState(userId, dto);
        return ResponseEntity.ok().build();
    }

    // 5분 주기로 사용자 러닝 정보들을 저장
    @PostMapping("/states")
    public ResponseEntity<Void> saveRunningStates(@RequestHeader("userId") Long userId,
                                                  @RequestHeader("routeId") Long routeId,
                                                  @RequestBody List<RunningPathDTO> dto) {
        realtimeRunningService.saveRunningStates(userId, routeId, dto);
        return ResponseEntity.ok().build();
    }

    // 러닝 완료 후 모든 정보를 저장
    @PostMapping("/complete")
    public ResponseEntity<Void> completeRunning(@RequestHeader("userId") Long userId,
                                                @RequestBody RunningCompleteRequest request) {
        //realtimeRunningService.completeRunning(token, request);
        realtimeRunningService.completeRunning(userId, request);
        realtimeRunningService.updateDailyRunningRecord(userId, LocalDate.now());
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
