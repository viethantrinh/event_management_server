package com.trvihnls.services;

import com.trvihnls.domains.Duty;
import com.trvihnls.dtos.duty.DutyCreateRequest;
import com.trvihnls.dtos.duty.DutyResponse;
import com.trvihnls.dtos.duty.DutyUpdateRequest;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.mappers.DutyMapper;
import com.trvihnls.repositories.DutyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DutyService {

    private final DutyRepository dutyRepository;
    private final DutyMapper dutyMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<DutyResponse> getAllDuties() {
        List<Duty> duties = dutyRepository.findAll();
        return duties.stream()
                .map(dutyMapper::fromDutyToDutyResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DutyResponse getDutyById(Integer dutyId) {
        Duty duty = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DUTY_NOT_EXISTED));
        return dutyMapper.fromDutyToDutyResponse(duty);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DutyResponse createDuty(DutyCreateRequest request) {
        // Check if duty with same name already exists
        if (dutyRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCodeEnum.DUTY_EXISTED);
        }

        Duty duty = dutyMapper.fromDutyCreateRequestToDuty(request);
        Duty savedDuty = dutyRepository.save(duty);
        return dutyMapper.fromDutyToDutyResponse(savedDuty);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DutyResponse updateDuty(Integer dutyId, DutyUpdateRequest request) {
        Duty duty = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DUTY_NOT_EXISTED));

        // Check if name is being updated and if it already exists
        if (request.getName() != null && !request.getName().equals(duty.getName())) {
            if (dutyRepository.existsByName(request.getName())) {
                throw new AppException(ErrorCodeEnum.DUTY_EXISTED);
            }
        }

        dutyMapper.updateDutyFromRequest(duty, request);
        Duty savedDuty = dutyRepository.save(duty);
        return dutyMapper.fromDutyToDutyResponse(savedDuty);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteDuty(Integer dutyId) {
        Duty duty = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DUTY_NOT_EXISTED));

        dutyRepository.delete(duty);
    }
}
