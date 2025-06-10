package com.example.runity.domain;

import com.example.runity.enums.Day;
import com.example.runity.enums.Place;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="routine")
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