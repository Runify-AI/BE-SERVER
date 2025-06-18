package com.example.runity.controller;

import com.example.runity.DTO.RouteRequestDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.DTO.RouteResponseDTO;
import com.example.runity.constants.SuccessCode;
import com.example.runity.domain.Route;
import com.example.runity.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java. util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
@Tag(name = "경로", description = "러닝 경로 관련 API")
public class RouteController {
    private final RouteService routeService;

    @Operation(summary = "경로 생성", description = "러닝 경로를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "경로 생성 성공",
                    content = @Content(schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReturnCodeDTO> createRoute(@RequestBody RouteRequestDTO routeRequestDTO) {
        routeService.createRoute(routeRequestDTO);
        return new ResponseEntity<>(
                new ReturnCodeDTO(201, SuccessCode.SUCCESS_ROUTE_CREATE.getMessage()),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "유저의 경로 목록 조회", description = "유저의 경로 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Route.class)))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Route>> getRoutesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(routeService.getRouteByUser(userId));
    }

    @Operation(summary = "하나의 경로 조회", description = "경로 ID로 경로를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Route.class))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content)
    })
    @GetMapping("/{routeId}")
    public ResponseEntity<Route> getRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getRouteById(routeId));
    }
    @Operation(summary = "경로 수정", description = "경로 ID로 경로를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content)
    })
    @PutMapping("/{routeId}")
    public ResponseEntity<ReturnCodeDTO> updateRoute(
            @PathVariable Long routeId,
            @RequestBody RouteRequestDTO routeRequestDTO) {

        routeService.updateRoute(routeId, routeRequestDTO);
        return ResponseEntity.ok(
                new ReturnCodeDTO(200, SuccessCode.SUCCESS_ROUTE_UPDATE.getMessage())
        );
    }

    @Operation(summary = "경로 삭제", description = "경로 ID로 경로를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content)
    })
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.noContent().build(); // 204
    }
}
