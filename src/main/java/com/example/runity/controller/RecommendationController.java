package com.example.runity.controller;

import com.example.runity.DTO.route.RecommendationResponseDTO;
import com.example.runity.DTO.route.*;
import com.example.runity.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<RecommendationRequestDTO> generate(@RequestHeader Long routeId) {
        RecommendationRequestDTO result = recommendationService.generateRecommendations(routeId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestHeader Long routeId, @RequestBody List<RecommendationResponseDTO> recommendations) {
        recommendationService.saveRecommendationResults(routeId, recommendations);
        return ResponseEntity.ok().build();
    }
}
