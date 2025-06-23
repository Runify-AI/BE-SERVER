package com.example.runity.DTO.history;

import com.example.runity.DTO.runningTS.FeedbackSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter @Setter
public class RunningHistoryDTO {
    private Long routeId;
    private Float distance;                      // 총 거리
    private Float duration;
    private Float averagePace;                       // 평균 페이스
    private Integer stopCount;
    private FeedbackSummary feedbackSummaryDTO;
    private Integer focusScore;
    private Integer effortLevel;                      // 힘듦 정도
    private String comment;

    private LocalTime completedTime;                  // 완료 시간
    private LocalTime elapsedTime;
    private List<RunningHistoryDetailDTO> runningTrackPoint;
}
