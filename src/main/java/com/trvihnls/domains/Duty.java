package com.trvihnls.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_duty")
public class Duty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 128)
    private String description;

    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventDetail> eventDetails = new HashSet<>();

    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventScore> eventScores = new HashSet<>();
}
