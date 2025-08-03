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
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(name = "work_email", length = 64, unique = true)
    private String workEmail;

    @Column(nullable = false, length = 256, unique = true)
    private String password;

    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Column(name = "academic_rank", length = 64)
    private String academicRank;

    @Column(name = "academic_degree", length = 64)
    private String academicDegree;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tbl_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventDetail> eventDetails = new HashSet<>();
}
