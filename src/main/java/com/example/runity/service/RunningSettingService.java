package com.example.runity.service;

import com.example.runity.DTO.route.RunningSettingsResponse;

public interface RunningSettingService {
    RunningSettingsResponse getRunningSettings(Long routeId);
}