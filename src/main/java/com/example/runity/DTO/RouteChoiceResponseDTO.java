package com.example.runity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteChoiceResponseDTO {
    private Long choiceId;
    private Boolean usePublicTransport;
    private Boolean preferSafePath;
    private Boolean avoidCrowdedAreas;
    private Boolean cycle;
}
