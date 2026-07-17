package com.teamsync.api.common.exception;

import com.teamsync.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception ex) {

        return ApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .build();

    }

}