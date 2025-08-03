package com.trvihnls.domains;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_event_detail")
@IdClass(EventDetail.EventDetailId.class)
public class EventDetail {
    @Id
    @Column(name = "event_id")
    private Integer eventId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
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

    // Composite key class
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventDetailId implements Serializable {
        private Integer eventId;
        private String userId;
        private Integer dutyId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventDetailId that = (EventDetailId) o;
            return Objects.equals(eventId, that.eventId) &&
                   Objects.equals(userId, that.userId) &&
                   Objects.equals(dutyId, that.dutyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventId, userId, dutyId);
        }
    }
}
