package com.epam.slsa.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"location"})
@Entity
@Table(name = "location_schedule")
public class LocationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private Day day;

    @Column(name = "start_working_time")
    private LocalTime startTime;

    @Column(name = "end_working_time")
    private LocalTime endTime;

    @ManyToOne
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationSchedule that = (LocationSchedule) o;
        return day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
