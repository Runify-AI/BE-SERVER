package com.example.runity.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PatchMapping("/live-location")
    public ResponseEntity<Map<String, Boolean>> updateLiveLocation(@RequestBody LiveLocationRequest request) {
        trackingService.saveLiveLocation(request);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
