package com.trvihnls.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotNull(message = "email must be not null")
    @Length(min = 3, message = "email must be not empty/blank, at least 3 characters")
    @Size(min = 3, max = 64, message = "email must between 3 and 64 characters")
    @Email(message = "email must be valid format")
    private String email;

    private String workEmail;

    @NotNull(message = "password must be not null")
    @Length(min = 3, message = "email must be not empty/blank, at least 3 characters")
    @Size(min = 8, max = 256, message = "password must between 8 and 256 characters")
    private String password;

    @NotNull(message = "full name must be not null")
    @Length(min = 2, message = "full name must be not empty/blank, at least 2 characters")
    @Size(min = 2, max = 128, message = "full name must between 2 and 128 characters")
    private String fullName;

    @NotNull(message = "phone number must be not null")
    @Length(min = 8, message = "phone number must be not empty/blank, at least 8 characters")
    @Size(min = 8, max = 16, message = "phone number must between 8 and 16 characters")
    private String phoneNumber;

    @NotNull(message = "academic rank must be not null")
    @Length(min = 2, message = "academic rank must be not empty/blank, at least 2 character")
    @Size(min = 2, max = 64, message = "academic rank must between 2 and 64 characters")
    private String academicRank;

    @NotNull(message = "academic degree must be not null")
    @Length(min = 2, message = "academic degree must be not empty/blank, at least 2 character")
    @Size(min = 2, max = 64, message = "academic degree must between 2 and 64 characters")
    private String academicDegree;
}
