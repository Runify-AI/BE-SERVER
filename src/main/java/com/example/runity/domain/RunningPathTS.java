package com.example.runity.domain;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public class RunningPathTS {
    private Instant timestamp;
    private double latitude;
    private double longitude;
    private double pace; // 분/km
    private float distance;
    private float speed;
    private LocalTime elapsedTime;
    private String type;
    private String semiType;
    private String message;

    // 생성자, getter, setter
    public RunningPathTS(Instant timestamp, double latitude, double longitude, double pace, float distance, float speed, LocalTime elapsedTime, String type, String semiType, String message) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.distance = distance;
        this.speed = speed;
        this.elapsedTime = elapsedTime;
        this.type = type;
        this.semiType = semiType;
        this.message = message;
    }

    public Instant getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getPace() { return pace; }
    public float getDistance() { return distance; }
    public float getSpeed() { return speed; }
    public LocalTime getElapsedTime() { return elapsedTime; }
    public String getType() { return type; }
    public String getSemiType() { return semiType; }
    public String getMessage() { return message; }
}
