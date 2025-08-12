package com.trvihnls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCodeEnum {
    GENERAL_SUCCESS(1000, HttpStatus.OK, "just success"),
    SIGN_IN_SUCCESS(1001, HttpStatus.OK, "sign in success"),
    SIGN_UP_SUCCESS(1002, HttpStatus.CREATED, "sign up success"),
    LIST_ALL_USER_SUCCESS(1003, HttpStatus.OK, "get all users success"),
    NO_CONTENT(1004, HttpStatus.NO_CONTENT, "no content")
    ;

    private final int code;
    private final HttpStatus status;
    private final String message;
}
