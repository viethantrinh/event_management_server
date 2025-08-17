package com.trvihnls.controllers;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.event.EventCreateRequest;
import com.trvihnls.dtos.event.EventDetailResponse;
import com.trvihnls.dtos.event.EventResponse;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.EventService;
import com.trvihnls.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> listAllEventsApi() {
        List<EventResponse> response = eventService.getAllEvents();
        if (response.isEmpty()) return ResponseUtils.success(SuccessCodeEnum.NO_CONTENT, response);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDetailResponse>> getEventByIdApi(@PathVariable Integer id) {
        EventDetailResponse response = eventService.getEventById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEventApi(@RequestBody EventCreateRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEventApi(@PathVariable Integer id, @RequestBody EventCreateRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEventApi(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
