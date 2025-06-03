package com.example.runity.domain;

import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long routineId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Place place;

    @Column(nullable = false)
    private LocalTime time;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_days", joinColumns = @JoinColumn(name = "routine_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private List<Day> day;

    public Routine(Long userId, Place place, LocalTime time, List<Day> day) {
        this.userId = userId;
        this.place = place;
        this.time = time;
        this.day = day;
    }

    public void update(Place place, LocalTime time, List<Day> day){
        this.place = place;
        this.time = time;
        this.day = day;
    }
}