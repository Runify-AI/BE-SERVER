package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class RunningHistoryDetailDTO {
    private Float distance;           // 이동한 거리
    private LocalTime elapsedTime;
    private Float pace;               // 페이스
    private LocalTime timeStamp;      // 타임스탬프
    private LocasionDTO location;     // 좌표
    private String type;
    private String semiType;
    private String message;
}