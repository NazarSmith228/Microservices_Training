package com.epam.slsa.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"locations", "coaches"})
@Entity
@Table(name = "sport_type")
@Slf4j
public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "sportTypes")
    private List<Location> locations = new ArrayList<>();

    @ManyToMany(mappedBy = "sportTypes")
    private List<Coach> coaches = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SportType casted = (SportType) o;
        return id == casted.id
                && Objects.equals(name.toLowerCase(), casted.name.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}

