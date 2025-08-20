package com.trvihnls.dtos.user;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<UserEventParticipation> eventParticipations;
    private Integer totalEvents;
    private Double totalScore;
}
