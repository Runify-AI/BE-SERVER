package com.example.runity.controller;

import com.example.runity.DTO.route.RunningSettingsResponse;
import com.example.runity.service.RunningSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/running-settings")
@RequiredArgsConstructor
public class RunningSettingController {

    private final RunningSettingService runningSettingService;

    /**
     * 특정 routeId에 대한 러닝 세팅 정보 반환
     */
    @GetMapping("/{routeId}")
    public ResponseEntity<RunningSettingsResponse> getRunningSettings(@PathVariable Long routeId) {
        RunningSettingsResponse response = runningSettingService.getRunningSettings(routeId);
        return ResponseEntity.ok(response);
    }
}
