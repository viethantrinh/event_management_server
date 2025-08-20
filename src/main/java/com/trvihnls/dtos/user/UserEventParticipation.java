package com.trvihnls.dtos.user;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEventParticipation {
    private Integer eventId;
    private String eventName;
    private String eventDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private String dutyName;
    private String dutyDescription;
    private Double score;
}
