package com.epam.slsa.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"location"})
@Entity
@Table(name = "photo")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image photo = (Image) o;
        return id == photo.id
                && Objects.equals(url, photo.url)
                && Objects.equals(location, photo.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, location);
    }
}
