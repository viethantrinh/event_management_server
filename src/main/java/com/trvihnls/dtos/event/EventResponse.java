package com.trvihnls.dtos.event;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private int id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
