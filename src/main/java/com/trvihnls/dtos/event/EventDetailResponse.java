package com.trvihnls.dtos.event;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DutyInfo> duties;
    private List<ParticipantInfo> participants;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DutyInfo {
        private Integer dutyId;
        private String dutyName;
        private Double score;
        private AssignedUserInfo assignedUser;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedUserInfo {
        private String userId;
        private String fullName;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantInfo {
        private String userId;
        private String fullName;
        private String academicRank;
        private String academicDegree;
        private AssignedDutyInfo assignedDuty;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedDutyInfo {
        private Integer dutyId;
        private String dutyName;
        private Double score;
    }
}
