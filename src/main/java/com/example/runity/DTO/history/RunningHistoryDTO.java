package com.example.runity.DTO.history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter @Setter
public class RunningHistoryDTO {
    private Float averagePace;                       // 평균 페이스
    private String comment;
    private LocalTime completedTime;                  // 완료 시간
    private Integer effortLevel;                      // 힘듦 정도
    private LocalTime elapsedTime;
    private Long routeId;                             // 루트 아이디
    private Float totalDistance;                      // 총 거리
    private List<RunningHistoryDetailDTO> runningTrackPoint;
}
