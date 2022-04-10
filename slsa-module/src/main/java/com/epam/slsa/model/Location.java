package com.epam.slsa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "admin_id")
    private Integer adminId;

    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<LocationSchedule> locationSchedule;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coach> coaches;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> photos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "location_sport_type",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_type_id"))
    private Set<SportType> sportTypes = new HashSet<>();

}
