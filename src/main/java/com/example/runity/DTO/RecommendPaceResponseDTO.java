package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecommendPaceResponseDTO {
    private Double recommendedPace;  // 추천된 페이스 (/km)
    private String feedbackMessage;  // 간단한 피드백 메시지
}
