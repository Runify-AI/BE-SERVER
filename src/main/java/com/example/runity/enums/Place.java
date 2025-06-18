package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Place {
    COMPANY("회사"),
    GYM("헬스장"),
    SCHOOL("학교"),
    HOME("집"),
    ETC("기타");

    private final String places;
    // 한글 -> 영어
    public static Place fromString(String places) {
        for (Place place : Place.values()) {
            if (place.getPlaces().equalsIgnoreCase(places)) {
                return place;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(Place place) {
        return place.getPlaces();
    }
}
