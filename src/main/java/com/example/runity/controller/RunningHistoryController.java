package com.example.runity.controller;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.service.RunningHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class RunningHistoryController {

    private final RunningHistoryService runningHistoryService;

    @GetMapping("/daily")
    public ResponseEntity<DailyRunningRecord> getDailyRecord(@RequestParam LocalDate date) {
        return ResponseEntity.ok(runningHistoryService.getDailyRecord(date));
    }

    @GetMapping("/period")
    public ResponseEntity<List<RunningHistoryDTO>> getPeriodRecord(@RequestParam LocalDate start,
                                                                   @RequestParam LocalDate end) {
        return ResponseEntity.ok(runningHistoryService.getPeriodRecord(start, end));
    }
}
