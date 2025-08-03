package com.trvihnls.domains;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_event_score")
@IdClass(EventScore.EventScoreId.class)
public class EventScore {
    @Id
    @Column(name = "event_id")
    private Integer eventId;

    @Id
    @Column(name = "duty_id")
    private Integer dutyId;

    @Column(nullable = false)
    private double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_id", insertable = false, updatable = false)
    private Duty duty;

    // Composite key class
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventScoreId implements Serializable {
        private Integer eventId;
        private Integer dutyId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventScoreId that = (EventScoreId) o;
            return Objects.equals(eventId, that.eventId) && Objects.equals(dutyId, that.dutyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventId, dutyId);
        }
    }
}
