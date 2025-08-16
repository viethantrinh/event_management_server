package com.trvihnls.dtos.duty;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DutyResponse {
    private Integer id;
    private String name;
    private String description;
}
