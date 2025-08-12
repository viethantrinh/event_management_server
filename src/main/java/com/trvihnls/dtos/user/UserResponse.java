package com.trvihnls.dtos.user;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String email;
    private String workEmail;
    private String fullName;
    private String phoneNumber;
    private String academicRank;
    private String academicDegree;
    private Set<RoleResponse> roles = new HashSet<>();

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleResponse implements Serializable {
        private String name;
        private String description;
    }
}
