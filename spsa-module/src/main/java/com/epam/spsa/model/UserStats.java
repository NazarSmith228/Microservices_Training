package com.epam.spsa.model;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_stats")
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "sport_type_id")
    private SportType sportType;

    @Column(name = "result_km")
    private double resultKm;

    @Column(name = "result_hours")
    private Duration resultH;

    @Column(name = "location_id")
    private int locationId;

    @Column(name = "coach_id")
    private int coachId;

    @Column(name = "insert_date")
    private LocalDate insertionDate;

}
