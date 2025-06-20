package com.example.runity.DTO;

import com.example.runity.enums.PreferencePlace;
import com.example.runity.enums.PreferenceRoute;
import com.example.runity.enums.PreferenceAvoid;
import com.example.runity.enums.PreferenceEtc;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;

import java.util.List;

@Getter
@Setter
public class PreferenceRequestDTO {
    @NotNull
    private List<PreferencePlace> preferencePlaces;

    @NotNull
    private List<PreferenceRoute> preferenceRoutes;

    @NotNull
    private List<PreferenceAvoid> preferenceAvoids;

    @NotNull
    private List<PreferenceEtc> preferenceEtcs;
}
