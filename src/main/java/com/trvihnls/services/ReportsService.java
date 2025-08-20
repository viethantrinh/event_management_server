package com.trvihnls.services;

import com.trvihnls.dtos.reports.OverviewUserData;
import com.trvihnls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<OverviewUserData> getOverviewReport() {
        List<Object[]> rawData = userRepository.findOverviewReportData();
        List<OverviewUserData> overviewData = new ArrayList<>();

        for (int i = 0; i < rawData.size(); i++) {
            Object[] row = rawData.get(i);

            OverviewUserData userData = OverviewUserData.builder()
                    .sequenceNumber(i + 1)
                    .fullName((String) row[0])
                    .department(row[1] != null ? (String) row[1] : "")
                    .degree(row[2] != null ? (String) row[2] : "")
                    .totalEventsParticipated(((Long) row[3]).intValue())
                    .totalScore((Double) row[4])
                    .build();

            overviewData.add(userData);
        }

        return overviewData;
    }
}
