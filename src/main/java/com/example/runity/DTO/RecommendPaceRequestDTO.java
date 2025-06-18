package com.example.runity.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class RecommendPaceRequestDTO {
    private Long userId;
    private Long routeId;
    private LocalDateTime timestamp;
}
