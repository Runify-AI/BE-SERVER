package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Day {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일");

    private final String days;
    // 한글 -> 영어
    public static Day fromString(String days) {
        for (Day day : Day.values()) {
            if (day.getDays().equalsIgnoreCase(days)) {
                return day;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(Day day){
        return day.getDays();
    }
}
