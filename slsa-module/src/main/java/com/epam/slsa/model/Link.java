package com.epam.slsa.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"coach"})
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkType type;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(url, link.url)
                && Objects.equals(type, link.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type);
    }

}
