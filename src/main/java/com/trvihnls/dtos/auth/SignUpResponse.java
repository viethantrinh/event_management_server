package com.trvihnls.dtos.auth;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private String token;
    private String email;
    private String workEmail;
    private String fullName;
    private String phoneNumber;
    private String academicRank;
    private String academicDegree;
}
