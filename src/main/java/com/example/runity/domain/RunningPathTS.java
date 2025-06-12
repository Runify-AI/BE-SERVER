package com.example.runity.domain;

import lombok.Builder;

import java.time.Instant;

@Builder
public class RunningPathTS {
    private Instant timestamp;
    private double latitude;
    private double longitude;
    private double pace; // 분/km
    private float distance;
    private float speed;

    // 생성자, getter, setter
    public RunningPathTS(Instant timestamp, double latitude, double longitude, double pace, float distance, float speed) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.distance = distance;
        this.speed = speed;
    }

    public Instant getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getPace() { return pace; }
    public float getDistance() { return distance; }
    public float getSpeed() { return speed; }
}
