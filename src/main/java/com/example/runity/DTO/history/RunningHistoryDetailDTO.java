package com.example.runity.DTO.history;

import com.example.runity.DTO.route.LocationDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class RunningHistoryDetailDTO {
    private Float distance;           // 이동한 거리
    private LocalTime elapsedTime;
    private Float pace;               // 페이스
    private LocalTime timeStamp;      // 타임스탬프
    private LocationDTO location;     // 좌표
    private Float typeEta;
    private Float typePace;
    private Float typeStop;
}