package com.example.runity.service;

import com.example.runity.DTO.runningTS.RunningPerformanceDTO;

public interface RunningRecommendationService {
    RunningPerformanceDTO evaluateRunningPerformance(String token);
}
