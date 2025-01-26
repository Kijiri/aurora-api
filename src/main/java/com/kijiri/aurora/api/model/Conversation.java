package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "conversation")
    private Set<ConversationParticipant> participants;
    
    @Column(nullable = false)
    private String title;
    private String avatarUrl;
    
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Instant creationDate;
}
