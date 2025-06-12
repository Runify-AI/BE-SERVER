package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RunningType {
    JOGGING("조깅"),
    HALF_MARATHON("하프 마라톤"),
    RUNNING("10K 러닝"),
    TRAIL_RUNNING("트레일 러닝"),
    INTERVAL_TRAINING("인터벌 러닝");

    private final String runningTypes;
    // 한글 -> 영어
    public static RunningType fromString(String runningTypes) {
        for (RunningType runningType : RunningType.values()) {
            if (runningType.getRunningTypes().equalsIgnoreCase(runningTypes)) {
                return runningType;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(RunningType runningType){
        return runningType.getRunningTypes();
    }
}
