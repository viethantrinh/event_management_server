package com.trvihnls.mappers;

import com.trvihnls.domains.User;
import com.trvihnls.dtos.user.UserResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse fromUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .workEmail(user.getWorkEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .academicRank(user.getAcademicRank())
                .academicDegree(user.getAcademicDegree())
                .roles(user.getRoles().stream()
                        .map(role -> UserResponse.RoleResponse.builder()
                                .name(role.getName())
                                .description(role.getDescription())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
