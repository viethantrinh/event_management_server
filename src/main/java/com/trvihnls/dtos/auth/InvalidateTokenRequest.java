package com.trvihnls.dtos.auth;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidateTokenRequest {
    private String token;
}


