package com.example.runity.tracking;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "실시간 위치 API", description = "컨트롤러에 대한 설명입니다.")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PatchMapping("/live-location")
    public ResponseEntity<Map<String, Boolean>> updateLiveLocation(@RequestBody LiveLocationRequest request) {
        trackingService.saveLiveLocation(request);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
