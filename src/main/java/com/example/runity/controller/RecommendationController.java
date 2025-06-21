package com.example.runity.controller;

import com.example.runity.DTO.route.RecommendationResponseDTO;
import com.example.runity.DTO.route.*;
import com.example.runity.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "AI 경로 추천")
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
            summary = "Request",
            description = "AI 예측에 필요한 데이터를 반환하는 API 입니다. [담당자] : 최효정"
    )
    @PostMapping("/generate")
    public ResponseEntity<RecommendationRequestDTO> generate(@RequestHeader("Authorization")String token, @RequestHeader Long routeId) {
        RecommendationRequestDTO result = recommendationService.generateRecommendations(token, routeId);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Response",
            description = "AI 예측 결과를 DB에 저장하는 API 입니다. [담당자] : 최효정"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestHeader Long routeId, @RequestBody List<RecommendationResponseDTO> recommendations) {
        recommendationService.saveRecommendationResults(routeId, recommendations);
        return ResponseEntity.ok().build();
    }
}
