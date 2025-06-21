package com.example.runity.service;

import com.example.runity.DTO.route.RunningSettingsResponse;
import org.springframework.stereotype.Service;

@Service
public interface RunningSettingService {
    RunningSettingsResponse getRunningSettings(Long routeId);
}
