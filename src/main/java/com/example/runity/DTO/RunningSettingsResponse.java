package com.example.runity.DTO;

//import com.example.runity.domain.Route;
import com.example.runity.domain.Routine;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class RunningSettingsResponse {
    //private Route route;               // 추천 경로 정보
    private Routine routine;           // 루틴 정보
    private Double recommendedPace;    // 추천 페이스 (/km)
}
