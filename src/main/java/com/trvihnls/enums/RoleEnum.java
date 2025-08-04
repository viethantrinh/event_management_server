package com.trvihnls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("ADMIN", "manage everything"),
    USER("USER", "user");

    private final String name;
    private final String description;
}
