package com.trvihnls.services;

import com.trvihnls.domains.EventDetail;
import com.trvihnls.repositories.EventDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventDetailService {

    private EventDetailRepository eventDetailRepository;

}
