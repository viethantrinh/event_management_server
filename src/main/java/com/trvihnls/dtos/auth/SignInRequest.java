package com.trvihnls.dtos.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @NotNull(message = "email must be not null")
    @Length(min = 3, message = "email must be not empty/blank")
    @Size(min = 3, max = 64, message = "email must between 3 and 64 characters")
//    @Email(message = "email must be valid format")
    private String email;

    @NotNull(message = "password must be not null")
    @Length(min = 3, message = "email must be not empty/blank")
//    @Size(min = 8, max = 256, message = "password must between 8 and 256 characters")
    private String password;
}
