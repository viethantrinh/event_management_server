package com.trvihnls.controllers;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.duty.DutyCreateRequest;
import com.trvihnls.dtos.duty.DutyResponse;
import com.trvihnls.dtos.duty.DutyUpdateRequest;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.DutyService;
import com.trvihnls.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/duties")
public class DutyController {

    private final DutyService dutyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DutyResponse>>> listAllDutiesApi() {
        List<DutyResponse> response = dutyService.getAllDuties();
        if (response.isEmpty()) return ResponseUtils.success(SuccessCodeEnum.NO_CONTENT, response);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/{dutyId}")
    public ResponseEntity<ApiResponse<DutyResponse>> getDutyByIdApi(@PathVariable Integer dutyId) {
        DutyResponse response = dutyService.getDutyById(dutyId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DutyResponse>> createDutyApi(
            @Valid @RequestBody DutyCreateRequest request) {
        DutyResponse response = dutyService.createDuty(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/{dutyId}")
    public ResponseEntity<ApiResponse<DutyResponse>> updateDutyApi(
            @PathVariable Integer dutyId,
            @Valid @RequestBody DutyUpdateRequest request) {
        DutyResponse response = dutyService.updateDuty(dutyId, request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @DeleteMapping("/{dutyId}")
    public ResponseEntity<ApiResponse<Void>> deleteDutyApi(@PathVariable Integer dutyId) {
        dutyService.deleteDuty(dutyId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
