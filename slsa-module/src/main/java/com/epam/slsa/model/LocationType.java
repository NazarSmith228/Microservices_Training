package com.epam.slsa.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"locations"})
@Entity
@Table(name = "location_type")
public class LocationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "locationType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

    @Enumerated(EnumType.STRING)
    @Column(name = "place")
    private Placing placing;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationType that = (LocationType) o;
        return id == that.id
                && Objects.equals(name.toLowerCase(), that.name.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
