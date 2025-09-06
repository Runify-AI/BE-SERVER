package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferenceRoute {
    NONE("상관없음"),
    FASTEST("빠른 길 우선"),
    SCENIC("경치 좋은 길"),
    EXERCISE("러닝/운동 경로"),
    QUIET("조용한 길");

    private final String userPreference;
    // 한글 -> 영어
    public static PreferenceRoute fromString(String userPreference) {
        for (PreferenceRoute pr : PreferenceRoute.values()) {
            if (pr.getUserPreference().equalsIgnoreCase(userPreference)) {
                return pr;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(PreferenceRoute preferenceRoute){
        return preferenceRoute.getUserPreference();
    }
}
