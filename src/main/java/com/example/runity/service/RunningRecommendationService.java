package com.example.runity.service;

import com.example.runity.DTO.RunningPerformanceDTO;

public interface RunningRecommendationService {
    RunningPerformanceDTO evaluateRunningPerformance(Long userId);
}
