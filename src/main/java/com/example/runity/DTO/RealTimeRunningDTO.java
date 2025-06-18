package com.example.runity.DTO;

import com.example.runity.domain.RunningPathTS;
import lombok.Builder;
import lombok.Getter;
import okhttp3.Route;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class RealTimeRunningDTO {
    private LocalDateTime startTime;// TODO: 시작 시간
    private LocalDateTime endTime;  // 완주 시간
    private boolean isCompleted;    //
    private String giveUpReason;    //
    private int effortLevel;        // 사용자 힘듦 정도
    private float avgPace;          //
    private float avgSpeed;         //
    private List<RunningPathTS> pathList;   // 좌표 리스트
    private List<Route> routeList;          // TODO: 원래 경로 좌표 리스트
}