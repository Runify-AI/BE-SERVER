package com.example.runity.controller;

import com.example.runity.DTO.RunningSettingsResponse;
import com.example.runity.service.RunningSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running-setting")
public class RunningSettingController {

    private final RunningSettingService runningSettingService;

    @GetMapping
    public ResponseEntity<RunningSettingsResponse> getRunningSetting(@RequestParam Long routeId) {
        RunningSettingsResponse response = runningSettingService.getRunningSettings(routeId);
        return ResponseEntity.ok(response);
    }
}
