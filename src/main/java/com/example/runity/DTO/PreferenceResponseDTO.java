package com.example.runity.DTO;

import com.example.runity.domain.Preference;
import com.example.runity.enums.PreferencePlace;
import com.example.runity.enums.PreferenceRoute;
import com.example.runity.enums.PreferenceAvoid;
import com.example.runity.enums.PreferenceEtc;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PreferenceResponseDTO {
    private Long preferenceId;
    private Long userId;

    private List<PreferencePlace> preferencePlaces;
    private List<PreferenceRoute> preferenceRoutes;
    private List<PreferenceAvoid> preferenceAvoids;
    private List<PreferenceEtc> preferenceEtcs;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public PreferenceResponseDTO(Preference preference) {
        this.preferenceId = preference.getPreferenceId();
        this.userId = preference.getUser().getUserId();
        this.preferencePlaces = preference.getPreferencePlaces();
        this.preferenceRoutes = preference.getPreferenceRoutes();
        this.preferenceAvoids = preference.getPreferenceAvoids();
        this.preferenceEtcs = preference.getPreferenceEtcs();
        this.createAt = preference.getCreatedAt();
        this.updateAt = preference.getUpdatedAt();
    }

    public PreferenceResponseDTO(Long preferenceId, Long userId,
                                 List<PreferencePlace> preferencePlaces,
                                 List<PreferenceRoute> preferenceRoutes,
                                 List<PreferenceAvoid> preferenceAvoids,
                                 List<PreferenceEtc> preferenceEtcs,
                                 LocalDateTime createAt, LocalDateTime updateAt) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.preferencePlaces = preferencePlaces;
        this.preferenceRoutes = preferenceRoutes;
        this.preferenceAvoids = preferenceAvoids;
        this.preferenceEtcs = preferenceEtcs;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
