package com.epam.spsa.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "criteria")
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "sport_type_id")
    private SportType sportType;

    @Column(name = "maturity")
    @Enumerated(EnumType.STRING)
    private Maturity maturity;

    @Column(name = "running_distance")
    private double runningDistance;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "activity_time")
    @Enumerated(EnumType.STRING)
    private ActivityTime activityTime;

}
