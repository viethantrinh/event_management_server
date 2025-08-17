package com.trvihnls.mappers;

import com.trvihnls.domains.Event;
import com.trvihnls.domains.EventDetail;
import com.trvihnls.domains.EventScore;
import com.trvihnls.dtos.event.EventCreateRequest;
import com.trvihnls.dtos.event.EventDetailResponse;
import com.trvihnls.dtos.event.EventResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventResponse fromEventToEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }

    public EventDetailResponse fromEventToEventDetailResponse(Event event) {
        // Group event details by duty to get assignments
        Map<Integer, EventDetail> dutyAssignments = event.getEventDetails().stream()
                .filter(detail -> detail.getDutyId() != null)
                .collect(Collectors.toMap(
                    EventDetail::getDutyId,
                    detail -> detail,
                    (existing, replacement) -> existing
                ));

        // Build duties list with assigned users
        List<EventDetailResponse.DutyInfo> duties = event.getEventScores().stream()
                .map(eventScore -> {
                    EventDetail assignment = dutyAssignments.get(eventScore.getDutyId());
                    EventDetailResponse.AssignedUserInfo assignedUser = null;

                    if (assignment != null && assignment.getUser() != null) {
                        assignedUser = EventDetailResponse.AssignedUserInfo.builder()
                                .userId(assignment.getUser().getId())
                                .fullName(assignment.getUser().getFullName())
                                .build();
                    }

                    return EventDetailResponse.DutyInfo.builder()
                            .dutyId(eventScore.getDutyId())
                            .dutyName(eventScore.getDuty().getName())
                            .score(eventScore.getScore())
                            .assignedUser(assignedUser)
                            .build();
                })
                .collect(Collectors.toList());

        // Build participants list
        List<EventDetailResponse.ParticipantInfo> participants = event.getEventDetails().stream()
                .filter(detail -> detail.getUser() != null)
                .map(detail -> {
                    EventDetailResponse.AssignedDutyInfo assignedDuty = null;

                    if (detail.getDutyId() != null && detail.getDuty() != null) {
                        // Find the score for this duty
                        Double score = event.getEventScores().stream()
                                .filter(eventScore -> eventScore.getDutyId().equals(detail.getDutyId()))
                                .findFirst()
                                .map(EventScore::getScore)
                                .orElse(null);

                        assignedDuty = EventDetailResponse.AssignedDutyInfo.builder()
                                .dutyId(detail.getDutyId())
                                .dutyName(detail.getDuty().getName())
                                .score(score)
                                .build();
                    }

                    return EventDetailResponse.ParticipantInfo.builder()
                            .userId(detail.getUser().getId())
                            .fullName(detail.getUser().getFullName())
                            .academicRank(detail.getUser().getAcademicRank())
                            .academicDegree(detail.getUser().getAcademicDegree())
                            .assignedDuty(assignedDuty)
                            .build();
                })
                .collect(Collectors.toList());

        return EventDetailResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .duties(duties)
                .participants(participants)
                .build();
    }

    public Event fromEventCreateRequestToEvent(EventCreateRequest request) {
        return Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
