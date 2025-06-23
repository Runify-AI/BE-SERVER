package com.example.runity.service;

import com.example.runity.DTO.route.RunningSettingsResponse;

public interface RunningSettingService {

    // 주어진 경로 ID에 해당하는 러닝 세팅 정보를 조회
    RunningSettingsResponse getRunningSettings(Long routeId);
}