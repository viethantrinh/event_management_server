package com.trvihnls.dtos.event;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipantInfo {
    private Integer sequenceNumber;
    private String userId;
    private String fullName;
    private String dutyName;
    private Double score;
}
