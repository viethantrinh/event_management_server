package com.trvihnls.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_invalidated_token")
public class InvalidatedToken {

    @Id
    @Column(name = "jwt_id")
    private String jwtId;

    @Column(name = "expiration_time")
    private Date expirationTime;
}
