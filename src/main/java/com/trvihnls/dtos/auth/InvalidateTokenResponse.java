package com.trvihnls.dtos.auth;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidateTokenResponse {
    private boolean valid;
}


