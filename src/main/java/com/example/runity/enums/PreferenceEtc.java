package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferenceEtc {
    NONE("상관없음"),
    PET("반려동물 동반"),
    ACCESSIBLE("휠체어/유모차 접근성"),
    CONVENIENCE("편의시설 경유(휴게소, 화장실 등)");

    private final String userPreference;
    // 한글 -> 영어
    public static PreferenceEtc fromString(String userPreference) {
        for (PreferenceEtc pe : PreferenceEtc.values()) {
            if (pe.getUserPreference().equalsIgnoreCase(userPreference)) {
                return pe;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(PreferenceEtc preferenceEtc){
        return preferenceEtc.getUserPreference();
    }
}
