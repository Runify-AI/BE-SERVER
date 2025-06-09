package com.example.runity.controller;

import com.example.runity.DTO.RunningCompleteRequest;
import com.example.runity.DTO.RunningPathDTO;
//import com.example.runity.DTO.EvaluationResult;
import com.example.runity.service.RealtimeRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/running")
public class RealtimeRunningController {

    private final RealtimeRunningService realtimeRunningService;

    @PostMapping("/state")
    public ResponseEntity<Void> saveRunningState(@RequestBody RunningPathDTO dto) {
        realtimeRunningService.saveRunningState(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> completeRunning(@RequestBody RunningCompleteRequest request) {
        realtimeRunningService.completeRunning(request);
        return ResponseEntity.ok().build();
    }

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
