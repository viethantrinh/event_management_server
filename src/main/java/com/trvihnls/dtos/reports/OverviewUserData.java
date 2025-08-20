package com.trvihnls.dtos.reports;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverviewUserData {
    private Integer sequenceNumber;
    private String fullName;
    private String department;
    private String degree;
    private Integer totalEventsParticipated;
    private Double totalScore;
}
