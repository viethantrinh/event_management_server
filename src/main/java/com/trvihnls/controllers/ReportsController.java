package com.trvihnls.controllers;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.event.EventReportResponse;
import com.trvihnls.dtos.reports.OverviewUserData;
import com.trvihnls.dtos.user.UserReportResponse;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.EventService;
import com.trvihnls.services.ReportsService;
import com.trvihnls.services.UserService;
import com.trvihnls.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/reports")
public class ReportsController {

    private final UserService userService;
    private final EventService eventService;
    private final ReportsService reportsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserReportResponse>> getUserReportApi(@PathVariable String userId) {
        UserReportResponse response = userService.getUserReport(userId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<EventReportResponse>> getEventReportApi(@PathVariable Integer eventId) {
        EventReportResponse response = eventService.getEventReport(eventId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<List<OverviewUserData>>> getOverviewReportApi() {
        List<OverviewUserData> response = reportsService.getOverviewReport();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
