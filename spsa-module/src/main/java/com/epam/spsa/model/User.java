package com.epam.spsa.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "events", "ownEvents", "forms" })
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "photo")
    private String photo;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "has_child")
    private boolean hasChildren;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "online")
    private boolean online;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Message> messages = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Form> forms = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Chat> userChats = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "attendee", fetch = FetchType.EAGER)
    private Set<Event> events;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Event> ownEvents;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && hasChildren == user.hasChildren
                && Objects.equals(name, user.name)
                && Objects.equals(surname, user.surname)
                && Objects.equals(password, user.password)
                && Objects.equals(email, user.email)
                && Objects.equals(phoneNumber, user.phoneNumber)
                && Objects.equals(photo, user.photo)
                && Objects.equals(dateOfBirth, user.dateOfBirth)
                && gender == user.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}