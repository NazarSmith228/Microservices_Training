package com.epam.spsa.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat")
@Builder
@Data
@ToString(exclude = {"users"})
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Message> chatMessages = new LinkedList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_chat",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new LinkedHashSet<>();

}