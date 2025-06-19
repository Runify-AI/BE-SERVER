package com.example.runity.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Builder
@Getter @Setter
public class FeedbackDTO {
    private LocalTime timeStamp;
    private String type;
    private String semiType;
    private String message;
}
