package com.trvihnls.exceptions;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.base.ApiResponse.ErrorResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
                ErrorCodeEnum.GENERAL_ERROR.getMessage().toLowerCase(),
                exception.getMessage().toLowerCase()
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(errorPath)
                .errors(errorMessages)
                .build();

        return ResponseUtils.error(ErrorCodeEnum.GENERAL_ERROR, errorResponse);
    }
}
