package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class RunningHistoryDTO {
    private LocalDate date;                          // 날짜
    private Double totalDistance;                    // 총 거리
    private Double avgSpeed;                         // 평균 속도
    private Integer runCount;                        // 횟수
    //private List<RunningHistory> runningHistories;   // 세션별 상세 기록 리스트
}
