package com.example.runity.controller;

import com.example.runity.domain.RouteChoice;
import com.example.runity.DTO.RouteChoiceRequestDTO;
import com.example.runity.DTO.RouteChoiceResponseDTO;
import com.example.runity.service.RouteChoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes/choices")
@RequiredArgsConstructor
public class RouteChoiceController {
    private final RouteChoiceService routeChoiceService;

    @PostMapping("/{routeId}")
    public ResponseEntity<RouteChoiceResponseDTO> createRouteChoice(
            @PathVariable Long routeId,
            @RequestBody @Valid RouteChoiceRequestDTO routeChoiceRequestDTO) {
        RouteChoice saved = routeChoiceService.saveChoice(routeId, routeChoiceRequestDTO);

        RouteChoiceResponseDTO routeChoiceResponseDTO = RouteChoiceResponseDTO.builder()
                .choiceId(saved.getChoiceId())
                .usePublicTransport(saved.isUsePublicTransport())
                .preferSafePath(saved.isPreferSafePath())
                .avoidCrowdedAreas(saved.isAvoidCrowdedAreas())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(routeChoiceResponseDTO);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<List<RouteChoiceResponseDTO>> getRouteChoice(@PathVariable Long routeId) {
        List<RouteChoice> routeChoices = routeChoiceService.getChoiceByRouteId(routeId);

        List<RouteChoiceResponseDTO> response = routeChoices.stream()
                .map(pref -> RouteChoiceResponseDTO.builder()
                        .choiceId(pref.getChoiceId())
                        .usePublicTransport(pref.isUsePublicTransport())
                        .preferSafePath(pref.isPreferSafePath())
                        .avoidCrowdedAreas(pref.isAvoidCrowdedAreas())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
