package com.trvihnls.services;

import com.trvihnls.domains.Duty;
import com.trvihnls.repositories.DutyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DutyService {


    private DutyRepository dutyRepository;

}
