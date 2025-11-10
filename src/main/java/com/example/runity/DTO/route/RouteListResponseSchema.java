package com.example.runity.DTO.route;

import com.example.runity.DTO.RunningSettingResponseDTO;
import com.example.runity.DTO.ReturnCodeDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// @Schema를 사용하여 이 클래스가 Swagger 문서의 응답 모델임을 명시합니다.
@Schema(description = "경로 목록 조회 응답 스키마 (Swagger 전용)")
public class RouteListResponseSchema extends ReturnCodeDTO<List<RunningSettingResponseDTO>> {

    // 이 클래스는 ReturnCodeDTO<RunningSettingResponseDTO> 타입을 문서에 명시합니다.
    // 필드를 추가하거나 구현할 필요는 없습니다.
}