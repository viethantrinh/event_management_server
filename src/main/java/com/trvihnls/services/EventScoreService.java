package com.trvihnls.services;

import com.trvihnls.domains.EventScore;
import com.trvihnls.repositories.EventScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventScoreService {

    private EventScoreRepository eventScoreRepository;

}
