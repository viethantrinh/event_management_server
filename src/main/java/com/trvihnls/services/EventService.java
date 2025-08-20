package com.trvihnls.services;

import com.trvihnls.domains.Event;
import com.trvihnls.domains.EventDetail;
import com.trvihnls.domains.EventScore;
import com.trvihnls.dtos.event.*;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.mappers.EventMapper;
import com.trvihnls.repositories.EventDetailRepository;
import com.trvihnls.repositories.EventRepository;
import com.trvihnls.repositories.EventScoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventDetailRepository eventDetailRepository;
    private final EventScoreRepository eventScoreRepository;
    private final EventMapper eventMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) return Collections.emptyList();
        return events.stream()
                .map(eventMapper::fromEventToEventResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public EventDetailResponse getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.EVENT_NOT_EXISTED));
        return eventMapper.fromEventToEventDetailResponse(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {
        // Check if event name already exists
        if (eventRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCodeEnum.EVENT_EXISTED);
        }

        // Validate assignments - ensure no user is assigned to multiple duties
        validateUserAssignments(request.getAssignments());

        // Create the event
        Event event = eventMapper.fromEventCreateRequestToEvent(request);
        Event savedEvent = eventRepository.save(event);

        // Create event scores if duties are provided
        if (request.getDuties() != null && !request.getDuties().isEmpty()) {
            Set<EventScore> eventScores = new HashSet<>();
            for (EventCreateRequest.DutyScoreRequest dutyScore : request.getDuties()) {
                EventScore eventScore = EventScore.builder()
                        .eventId(savedEvent.getId())
                        .dutyId(dutyScore.getDutyId())
                        .score(dutyScore.getScore())
                        .build();
                eventScores.add(eventScore);
            }
            eventScoreRepository.saveAll(eventScores);
        }

        // Create event assignments if provided
        if (request.getAssignments() != null && !request.getAssignments().isEmpty()) {
            Set<EventDetail> eventDetails = new HashSet<>();
            for (EventCreateRequest.AssignmentRequest assignment : request.getAssignments()) {
                EventDetail eventDetail = EventDetail.builder()
                        .eventId(savedEvent.getId())
                        .userId(assignment.getUserId())
                        .dutyId(assignment.getDutyId())
                        .build();
                eventDetails.add(eventDetail);
            }
            eventDetailRepository.saveAll(eventDetails);
        }

        return eventMapper.fromEventToEventResponse(savedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public EventResponse updateEvent(Integer id, EventCreateRequest request) {
        // Check if event exists
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.EVENT_NOT_EXISTED));

        // Check if the new name conflicts with another event
        if (!existingEvent.getName().equals(request.getName()) &&
            eventRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCodeEnum.EVENT_EXISTED);
        }

        // Validate assignments - ensure no user is assigned to multiple duties
        validateUserAssignments(request.getAssignments());

        // Update event basic information
        existingEvent.setName(request.getName());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setStartDate(request.getStartDate());
        existingEvent.setEndDate(request.getEndDate());

        Event updatedEvent = eventRepository.save(existingEvent);

        // Only update EventScore if duties are explicitly provided in the request
        if (request.getDuties() != null) {
            // Delete existing event scores using bulk delete query
            entityManager.createQuery("DELETE FROM EventScore es WHERE es.eventId = :eventId")
                    .setParameter("eventId", id)
                    .executeUpdate();

            // Create new event scores if duties are provided
            if (!request.getDuties().isEmpty()) {
                Set<EventScore> eventScores = new HashSet<>();
                for (EventCreateRequest.DutyScoreRequest dutyScore : request.getDuties()) {
                    EventScore eventScore = EventScore.builder()
                            .eventId(updatedEvent.getId())
                            .dutyId(dutyScore.getDutyId())
                            .score(dutyScore.getScore())
                            .build();
                    eventScores.add(eventScore);
                }
                eventScoreRepository.saveAll(eventScores);
            }
        }

        // Only update EventDetail if assignments are explicitly provided in the request
        if (request.getAssignments() != null) {
            // Delete existing event details using bulk delete query
            entityManager.createQuery("DELETE FROM EventDetail ed WHERE ed.eventId = :eventId")
                    .setParameter("eventId", id)
                    .executeUpdate();

            // Create new event assignments if provided and not empty
            if (!request.getAssignments().isEmpty()) {
                Set<EventDetail> eventDetails = new HashSet<>();
                for (EventCreateRequest.AssignmentRequest assignment : request.getAssignments()) {
                    EventDetail eventDetail = EventDetail.builder()
                            .eventId(updatedEvent.getId())
                            .userId(assignment.getUserId())
                            .dutyId(assignment.getDutyId())
                            .build();
                    eventDetails.add(eventDetail);
                }
                eventDetailRepository.saveAll(eventDetails);
            }
        }

        // Flush to ensure all changes are executed
        entityManager.flush();

        // Clear the persistence context to ensure fresh data
        entityManager.clear();

        return eventMapper.fromEventToEventResponse(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteEvent(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.EVENT_NOT_EXISTED));

        // Use EntityManager for efficient bulk delete operations
        // Delete existing event scores using bulk delete query
        entityManager.createQuery("DELETE FROM EventScore es WHERE es.eventId = :eventId")
                .setParameter("eventId", id)
                .executeUpdate();

        // Delete existing event details using bulk delete query
        entityManager.createQuery("DELETE FROM EventDetail ed WHERE ed.eventId = :eventId")
                .setParameter("eventId", id)
                .executeUpdate();

        // Flush to ensure deletes are executed before deleting the event
        entityManager.flush();

        // Delete the event itself
        eventRepository.delete(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public EventReportResponse getEventReport(Integer eventId) {
        // Validate event exists
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.EVENT_NOT_EXISTED));

        // Get event participants
        List<EventParticipantInfo> participants = eventRepository.findEventParticipants(eventId);

        // Update sequence numbers
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setSequenceNumber(i + 1);
        }

        // Get duty distribution
        List<Object[]> dutyDistributionData = eventRepository.findDutyDistribution(eventId);
        Map<String, Integer> dutyDistribution = new HashMap<>();
        for (Object[] row : dutyDistributionData) {
            dutyDistribution.put((String) row[0], ((Long) row[1]).intValue());
        }

        // Calculate statistics
        int totalParticipants = participants.size();

        // Build and return response
        return EventReportResponse.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .eventDescription(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .participants(participants)
                .totalParticipants(totalParticipants)
                .dutyDistribution(dutyDistribution)
                .build();
    }

    /**
     * Validate that no user is assigned to multiple duties in the same event
     */
    private void validateUserAssignments(List<EventCreateRequest.AssignmentRequest> assignments) {
        if (assignments == null || assignments.isEmpty()) {
            return;
        }

        // Extract all user IDs from assignments
        Set<String> assignedUserIds = new HashSet<>();

        for (EventCreateRequest.AssignmentRequest assignment : assignments) {
            String userId = assignment.getUserId();

            // Check if this user is already assigned to another duty
            if (assignedUserIds.contains(userId)) {
                throw new AppException(ErrorCodeEnum.USER_ALREADY_ASSIGNED_IN_EVENT);
            }

            assignedUserIds.add(userId);
        }
    }
}
