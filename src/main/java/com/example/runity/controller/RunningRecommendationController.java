package com.example.runity.controller;

import com.example.runity.DTO.runningTS.RunningPerformanceDTO;
import com.example.runity.service.RunningRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
@Tag(name = "Running Recommendation", description = "AI 러닝 추천을 위한 입력 데이터 API")
public class RunningRecommendationController {

    private final RunningRecommendationService recommendationService;

    @Operation(
            summary = "러닝 추천 입력 데이터 생성",
            description = "사용자 ID 기반으로 AI 추천 입력용 데이터를 생성하여 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 입력 데이터 생성 완료"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 기록 없음")
    })
    @GetMapping("/input")
    public ResponseEntity<RunningPerformanceDTO> getRecommendationInput(
            @RequestHeader("Authorization")String token
    ) {
        return ResponseEntity.ok(recommendationService.evaluateRunningPerformance(token));
    }
}
