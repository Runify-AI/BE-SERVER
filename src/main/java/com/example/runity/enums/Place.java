package com.example.runity.enums;

import com.example.runity.constants.ErrorCode;
import com.example.runity.error.CustomException;
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

    private final String routinePlace;

    public static Place fromString(String routinePlace) {
        for (Place place : Place.values()) {
            if (place.getRoutinePlace().equalsIgnoreCase(routinePlace)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_PLACE, "유효하지 않은 장소 값입니다: " + routinePlace);
    }

    public static String toString(Place place) {
        return place.getRoutinePlace();
    }
}
