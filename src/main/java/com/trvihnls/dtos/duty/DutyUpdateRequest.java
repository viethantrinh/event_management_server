package com.trvihnls.dtos.duty;

import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DutyUpdateRequest {

    @Size(max = 64, message = "Name must be at most 64 characters")
    private String name;

    @Size(max = 128, message = "Description must be at most 128 characters")
    private String description;
}

