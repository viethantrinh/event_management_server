package com.trvihnls.mappers;

import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.dtos.user.UserResponse;
import com.trvihnls.dtos.user.UserUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.Set;
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

    public void updateUserFromRequest(User user, UserUpdateRequest request, Set<Role> roles) {
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getWorkEmail() != null) {
            user.setWorkEmail(request.getWorkEmail());
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAcademicRank() != null) {
            user.setAcademicRank(request.getAcademicRank());
        }
        if (request.getAcademicDegree() != null) {
            user.setAcademicDegree(request.getAcademicDegree());
        }
        if (roles != null) {
            user.setRoles(roles);
        }
    }
}
