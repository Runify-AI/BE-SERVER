package com.example.runity.controller;

import com.example.runity.DTO.RunningSettingResponseDTO;
import com.example.runity.DTO.route.RouteRequestDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import com.example.runity.DTO.route.RouteResponseDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java. util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "경로", description = "< 경로 > API")
public class RouteController {
    private final RouteService routeService;

    @Operation(summary = "경로를 생성하는 API 입니다. [담당자] : 정현아, 최효정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "경로 생성 성공",
                    content = @Content(schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/routes-create")
    public ResponseEntity<ReturnCodeDTO> createRoute(@RequestHeader("Authorization") String token,
                                                     @RequestBody RouteRequestDTO routeRequestDTO) {
        RouteResponseDTO routeResponseDTO = routeService.createRoute(token, routeRequestDTO);

        return ResponseEntity.status(SuccessCode.SUCCESS_ROUTE_CREATE.getStatus())
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTE_CREATE.getStatus(), SuccessCode.SUCCESS_ROUTE_CREATE.getMessage(), routeResponseDTO));
    }

    @Operation(summary = "경로 목록을 조회하는 API 입니다. [담당자] : 정현아, 최효정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경로 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Route.class)))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/routes-list")
    public ResponseEntity<ReturnCodeDTO> getRoutesByUser(@RequestHeader("Authorization") String token) {
        List<RunningSettingResponseDTO> routes = routeService.getRouteByUser(token);
        return ResponseEntity.ok()
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTE_LIST.getStatus(), SuccessCode.SUCCESS_ROUTE_LIST.getMessage(), routes));
    }

    @Operation(summary = "하나의 경로를 조회하는 API 입니다. [담당자] : 정현아, 최효정", description = "경로 ID로 하나의 경로를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하나의 경로 조회 성공",
                    content = @Content(schema = @Schema(implementation = Route.class))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/routes-one/{routeId}")
    public ResponseEntity<ReturnCodeDTO> getRoute(@PathVariable Long routeId) {
        RunningSettingResponseDTO route = routeService.getRouteById(routeId);
        return ResponseEntity.ok()
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTE_DETAIL.getStatus(), SuccessCode.SUCCESS_ROUTE_DETAIL.getMessage(), route));
    }
    @Operation(summary = "경로를 수정하는 API 입니다. [담당자] : 정현아", description = "경로 ID로 경로를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "경로 수정 성공",
                    content = @Content(schema = @Schema(implementation = ReturnCodeDTO.class))),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/routes-update/{routeId}")
    public ResponseEntity<ReturnCodeDTO> updateRoute(
            @RequestHeader("Authorization") String token,
            @PathVariable Long routeId,
            @RequestBody RouteRequestDTO routeRequestDTO) {

        routeService.updateRoute(token, routeId, routeRequestDTO);
        return ResponseEntity.ok()
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTE_UPDATE.getStatus(), SuccessCode.SUCCESS_ROUTE_UPDATE.getMessage(), null));
    }

    @Operation(summary = "경로를 삭제하는 API 입니다. [담당자] : 정현아", description = "경로 ID로 경로를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "경로 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "경로 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @DeleteMapping("/routes-delete/{routeId}")
    public ResponseEntity<ReturnCodeDTO> deleteRoute(
            @RequestHeader("Authorization") String token,
            @PathVariable Long routeId) {
        routeService.deleteRoute(token, routeId);
        return ResponseEntity.status(SuccessCode.SUCCESS_ROUTE_DELETE.getStatus())
                .body(new ReturnCodeDTO(SuccessCode.SUCCESS_ROUTE_DELETE.getStatus(), SuccessCode.SUCCESS_ROUTE_DELETE.getMessage()));
    }

    @GetMapping("/{routeId}/select-path/{pathId}")
    public ResponseEntity<ReturnCodeDTO> selectPath(
            @PathVariable Long routeId,
            @PathVariable Integer pathId
    ) {
        routeService.selectPath(routeId, pathId);
        return ResponseEntity.ok(
                new ReturnCodeDTO(SuccessCode.SUCCESS_SELECT_PATH.getStatus(),
                        SuccessCode.SUCCESS_SELECT_PATH.getMessage(),
                        null)
        );
    }
}
