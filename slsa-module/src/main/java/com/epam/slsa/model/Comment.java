package com.epam.slsa.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@ToString(exclude = {"coach"})
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_user")
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coach")
    private Coach coach;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private int rating;

}
