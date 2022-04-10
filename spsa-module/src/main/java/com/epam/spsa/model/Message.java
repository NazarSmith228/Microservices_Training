package com.epam.spsa.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"sender", "chat"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "message")
    private String message;

    @Column(name = "message_date")
    private LocalDateTime sendingDate;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

}