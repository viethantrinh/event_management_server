package com.trvihnls.mappers;

import com.trvihnls.domains.Duty;
import com.trvihnls.dtos.duty.DutyCreateRequest;
import com.trvihnls.dtos.duty.DutyResponse;
import com.trvihnls.dtos.duty.DutyUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class DutyMapper {

    public DutyResponse fromDutyToDutyResponse(Duty duty) {
        return DutyResponse.builder()
                .id(duty.getId())
                .name(duty.getName())
                .description(duty.getDescription())
                .build();
    }

    public Duty fromDutyCreateRequestToDuty(DutyCreateRequest request) {
        return Duty.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public void updateDutyFromRequest(Duty duty, DutyUpdateRequest request) {
        if (request.getName() != null) {
            duty.setName(request.getName());
        }
        if (request.getDescription() != null) {
            duty.setDescription(request.getDescription());
        }
    }
}
