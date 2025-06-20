package com.example.runity.domain;

import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name="routines")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
public class Routine {
    // 루틴설정 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long routineId;
    // 유저 ID
    @Column(nullable = false)
    private Long userId;
    // 장소
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Place place;
    // 시간
    @Column(nullable = false)
    private LocalTime time;
    // 요일
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_days", joinColumns = @JoinColumn(name = "routine_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private List<Day> day;
    // 좌표 목록
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_coordinates", joinColumns = @JoinColumn(name = "routine_id"))
    private Set<Routine.Coordinate> coordinates = new HashSet<>();

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private double latitude; // 위도
        private double longitude; // 경도

        // 중복 제거
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinate)) return false;
            Coordinate that = (Coordinate) o;
            return Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(latitude, longitude);
        }
    }

    public Routine(Long userId, Place place, LocalTime time, List<Day> day, Collection<Coordinate> coordinates) {
        this.userId = userId;
        this.place = place;
        this.time = time;
        this.day = day;
        this.coordinates.clear();
        if(coordinates != null) {
            this.coordinates.addAll(coordinates);
        }
    }

    public void update(Place place, LocalTime time, List<Day> day, Collection<Coordinate> coordinates) {
        this.place = place;
        this.time = time;
        this.day = day;
        this.coordinates.clear();
        if(coordinates != null) {
            this.coordinates.addAll(coordinates);
        }
    }
}