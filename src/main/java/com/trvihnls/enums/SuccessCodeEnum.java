package com.trvihnls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCodeEnum {
    GENERAL_SUCCESS(1000, HttpStatus.OK, "just success"),
    SIGN_IN_SUCCESS(1001, HttpStatus.OK, "sign in success");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
