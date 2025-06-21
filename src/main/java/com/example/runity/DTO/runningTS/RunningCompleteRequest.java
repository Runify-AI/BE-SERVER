package com.example.runity.DTO.runningTS;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class RunningCompleteRequest {
    private List<RunningPathDTO> runningPaths;  // 실시간 기록 경로들
    private LocalDateTime completeTime;         // 러닝 종료 시간
    private int effortLevel;                    // 사용자 힘듦 정도
    private String comment;                     // 자유 코멘트
    private Long routeId;                       // 원래 루트 아이디
}
