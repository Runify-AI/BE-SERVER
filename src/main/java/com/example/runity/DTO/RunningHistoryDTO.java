package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter @Setter
public class RunningHistoryDTO {
    private LocalDate date;                          // 날짜
    private Float totalDistance;                    // 총 거리
    private LocalTime totalRunTime;
    private Float avgSpeed;                         // 평균 속도
    private Integer runCount;                        // 횟

}
