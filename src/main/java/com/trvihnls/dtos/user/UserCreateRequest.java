package com.trvihnls.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 64, message = "Email must be at most 64 characters")
    private String email;

    @Email(message = "Work email should be valid")
    @Size(max = 64, message = "Work email must be at most 64 characters")
    private String workEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 256, message = "Password must be between 6 and 256 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 128, message = "Full name must be at most 128 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 16, message = "Phone number must be at most 16 characters")
    private String phoneNumber;

    @Size(max = 64, message = "Academic rank must be at most 64 characters")
    private String academicRank;

    @Size(max = 64, message = "Academic degree must be at most 64 characters")
    private String academicDegree;

    private Set<String> roleNames;
}
