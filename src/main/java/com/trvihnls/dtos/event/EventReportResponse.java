package com.trvihnls.dtos.event;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReportResponse {
    private Integer eventId;
    private String eventName;
    private String eventDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<EventParticipantInfo> participants;
    private Integer totalParticipants;
    private Map<String, Integer> dutyDistribution;
}
