package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class RunningCompleteRequest {
    private Long userId;
    private List<RunningPathDTO> runningPaths;  // 실시간 기록 경로들
    private LocalDateTime completeTime;         // 러닝 종료 시간
    private int effortLevel;                    // 사용자 힘듦 정도
    private String comment;                     // 자유 코멘트
}
