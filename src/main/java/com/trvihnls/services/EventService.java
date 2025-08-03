package com.trvihnls.services;

import com.trvihnls.domains.Event;
import com.trvihnls.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private EventRepository eventRepository;

}
