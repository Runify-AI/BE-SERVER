package com.example.runity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteChoiceRequestDTO {
    @NotNull
    private Boolean usePublicTransport;

    @NotNull
    private Boolean preferSafePath;

    @NotNull
    private Boolean avoidCrowdedAreas;

    @NotNull
    private Boolean cycle;
}
