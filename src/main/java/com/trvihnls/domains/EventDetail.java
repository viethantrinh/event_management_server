package com.trvihnls.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_event_detail", uniqueConstraints = {
        @UniqueConstraint(name = "uq_event_duty", columnNames = {"event_id", "duty_id"})
})
public class EventDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "duty_id")
    private Integer dutyId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_id", insertable = false, updatable = false)
    private Duty duty;
}
