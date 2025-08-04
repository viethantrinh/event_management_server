package com.trvihnls.exceptions;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.base.ApiResponse.ErrorResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception exception,
            HttpServletRequest request
    ) {
        String errorPath = request.getServletPath();

        List<String> errorMessages = List.of(
                ErrorCodeEnum.GENERAL_ERROR.getMessage().toLowerCase()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(errorPath)
                .errors(errorMessages)
                .build();

        log.error(exception.getMessage(), exception);
        return ResponseUtils.error(ErrorCodeEnum.GENERAL_ERROR, errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAppException(
            AppException exception,
            HttpServletRequest request
    ) {
        String errorPath = request.getServletPath();

        List<String> errorMessages = List.of(
                exception.getErrorCodeEnum().getMessage()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(errorPath)
                .errors(errorMessages)
                .build();

        log.error(exception.getMessage(), exception);
        return ResponseUtils.error(exception.getErrorCodeEnum(), errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String errorPath = request.getServletPath();

        List<String> errorMessages = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        for (FieldError e : fieldErrors) {
            errorMessages.add(e.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(errorPath)
                .errors(errorMessages)
                .build();

        log.error("Bad request: ", exception);
        return ResponseUtils.error(ErrorCodeEnum.BAD_REQUEST, errorResponse);
    }


}
