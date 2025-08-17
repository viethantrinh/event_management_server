package com.trvihnls.services;

import com.trvihnls.domains.Event;
import com.trvihnls.domains.EventDetail;
import com.trvihnls.domains.EventScore;
import com.trvihnls.dtos.event.EventCreateRequest;
import com.trvihnls.dtos.event.EventDetailResponse;
import com.trvihnls.dtos.event.EventResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.mappers.EventMapper;
import com.trvihnls.repositories.EventDetailRepository;
import com.trvihnls.repositories.EventRepository;
import com.trvihnls.repositories.EventScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventDetailRepository eventDetailRepository;
    private final EventScoreRepository eventScoreRepository;
    private final EventMapper eventMapper;

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

        // Update event basic information
        existingEvent.setName(request.getName());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setStartDate(request.getStartDate());
        existingEvent.setEndDate(request.getEndDate());

        Event updatedEvent = eventRepository.save(existingEvent);

        // Remove existing event scores and details
        eventScoreRepository.deleteAll(existingEvent.getEventScores());
        eventDetailRepository.deleteAll(existingEvent.getEventDetails());

        // Create new event scores if duties are provided
        if (request.getDuties() != null && !request.getDuties().isEmpty()) {
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

        // Create new event assignments if provided
        if (request.getAssignments() != null && !request.getAssignments().isEmpty()) {
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

        return eventMapper.fromEventToEventResponse(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteEvent(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.EVENT_NOT_EXISTED));

        // Delete related records first (due to foreign key constraints)
        eventScoreRepository.deleteAll(event.getEventScores());
        eventDetailRepository.deleteAll(event.getEventDetails());

        // Delete the event itself
        eventRepository.delete(event);
    }
}
