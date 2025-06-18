package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferenceAvoid {
    NONE("상관없음"),
    SLOPE("언덕/경사로"),
    STAIRS("계단"),
    DARK("어두운 길"),
    ISOLATED("인적이 드문 곳");

    private final String userPreference;
    // 한글 -> 영어
    public static PreferenceAvoid fromString(String userPreference) {
        for (PreferenceAvoid pa : PreferenceAvoid.values()) {
            if (pa.getUserPreference().equalsIgnoreCase(userPreference)) {
                return pa;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(PreferenceAvoid preferenceAvoid){
        return preferenceAvoid.getUserPreference();
    }
}
