package com.example.runity.DTO;

import com.example.runity.DTO.route.RecommendedPathsDTO;
import com.example.runity.DTO.route.RoutineResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunningSettingResponseDTO {

    private Long routeId;  // 입력받은 route_id 그대로
    private String startPoint;
    private String endPoint;
    private RoutineResponseDTO routineResponseDTO;  // 추천된 루틴 정보
    private boolean completed;  // 루틴 완료 여부
    private Integer selectedPath;  // 선택된 경로 (e.g. 추천된 pathId 중 하나)
    private List<RecommendedPathsDTO> paths;  // 전체 추천 경로 리스트
}

