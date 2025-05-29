package com.example.runity.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final RealTimeTrackingRepository trackingRepository;

    public void saveLiveLocation(LiveLocationRequest request) {
        RealTimeTrackingEntity tracking = new RealTimeTrackingEntity();
        tracking.setRouteId(request.getRouteId());
        tracking.setCurrentLocation(request.getCurrentLocation());
        tracking.setSpeed(request.getSpeed());
        tracking.setHeartbeat(request.getHeartbeat());
        tracking.setOutOfRoute(request.isOutOfRoute());
        tracking.setCreateAt(LocalDateTime.now());
        trackingRepository.save(tracking);
    }
}
