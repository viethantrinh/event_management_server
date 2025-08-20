package com.trvihnls.dtos.reports;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverviewReportResponse {
    private List<OverviewUserData> userData;
}
