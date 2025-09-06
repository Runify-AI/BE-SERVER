package com.example.runity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreferencePlace {
    NONE("상관없음"),
    PARK("공원"),
    CAFE("카페"),
    RIVER("강변/하천");

    private final String userPreference;
    // 한글 -> 영어
    public static PreferencePlace fromString(String userPreference) {
        for (PreferencePlace pp : PreferencePlace.values()) {
            if (pp.getUserPreference().equalsIgnoreCase(userPreference)) {
                return pp;
            }
        }
        return null;
    }
    // 영어 -> 한글
    public static String toString(PreferencePlace preferencePlace){
        return preferencePlace.getUserPreference();
    }
}
