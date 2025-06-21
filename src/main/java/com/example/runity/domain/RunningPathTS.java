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
    private Float typeEta;
    private Float typePace;
    private Float typeStop;


    // 생성자, getter, setter
    public RunningPathTS(Instant timestamp, double latitude, double longitude, double pace, float distance, float speed, LocalTime elapsedTime, Float typeEta, Float typePace, Float typeStop) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.distance = distance;
        this.speed = speed;
        this.elapsedTime = elapsedTime;
        this.typeEta = typeEta;
        this.typePace = typePace;
        this.typeStop = typeStop;
    }

    public Instant getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getPace() { return pace; }
    public float getDistance() { return distance; }
    public float getSpeed() { return speed; }
    public LocalTime getElapsedTime() { return elapsedTime; }
    public Float getTypeEta() { return typeEta; }
    public Float getTypePace() { return typePace; }
    public Float getTypeStop() { return typeStop; }
}
