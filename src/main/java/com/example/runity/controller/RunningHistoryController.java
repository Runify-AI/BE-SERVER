package com.example.runity.controller;

import com.example.runity.DTO.RunningHistoryDTO;
import com.example.runity.DTO.RunningHistoryDetailDTO;
import com.example.runity.DTO.RunningSessionDTO;
import com.example.runity.domain.DailyRunningRecord;
import com.example.runity.service.RunningHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class RunningHistoryController {

    private final RunningHistoryService runningHistoryService;

    @GetMapping("/daily")
    public ResponseEntity<List<RunningSessionDTO>> getDailyRecord(@RequestParam Long userId,
                                                            @RequestParam LocalDate date) {
        return ResponseEntity.ok(runningHistoryService.getDailyRecord(userId, date));
    }

    /*
    @GetMapping("/period")
    public ResponseEntity<List<RunningHistoryDetailDTO>> getPeriodRecord(@RequestHeader("Authorization") String token,
                                                                         @RequestParam LocalDate start,
                                                                   @RequestParam LocalDate end) {
        List<RunningHistoryDetailDTO> result = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            try {
                RunningHistoryDetailDTO dto = runningHistoryService.getDailyRecord(token, date);
                result.add(dto);
            } catch (RuntimeException e) {
                // 기록 없는 날짜는 무시
            }
        }
        return ResponseEntity.ok(result);
    }
    */
}
