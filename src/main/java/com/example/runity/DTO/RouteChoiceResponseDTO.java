package com.example.runity.DTO;

import com.example.runity.domain.RouteChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    /*
    public RouteChoiceResponseDTO(List<RouteChoice> routeChoices) {
        if (routeChoices != null && !routeChoices.isEmpty()) {
            RouteChoice latestChoice = routeChoices.get(routeChoices.size() - 1);
            this.choiceId = latestChoice.getChoiceId();
            this.usePublicTransport = latestChoice.isUsePublicTransport();
            this.preferSafePath = latestChoice.isPreferSafePath();
            this.avoidCrowdedAreas = latestChoice.isAvoidCrowdedAreas();
            this.cycle = latestChoice.isCycle();
        }
    }

     */
    public static RouteChoiceResponseDTO from(RouteChoice routeChoice) {
        return RouteChoiceResponseDTO.builder()
                .choiceId(routeChoice.getChoiceId())
                .usePublicTransport(routeChoice.isUsePublicTransport())
                .preferSafePath(routeChoice.isPreferSafePath())
                .avoidCrowdedAreas(routeChoice.isAvoidCrowdedAreas())
                .cycle(routeChoice.isCycle())
                .build();
    }
}
