package com.example.runity.controller;

import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.service.RunningSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/running/settings")
public class RunningPreparationController {

    private final RunningSettingService runningSettingService;

    @GetMapping
    public ResponseEntity<RunningSettingsResponse> recommendPace(@RequestParam Long userId) {
        return ResponseEntity.ok(runningSettingService.getRunningSettings(userId));
    }
}
