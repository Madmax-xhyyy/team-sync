package com.teamsync.api.common.exception;

import com.teamsync.api.common.response.ApiError;
import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.common.response.ValidationError;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {

    ApiError error = ApiError.builder()
          .success(false)
          .message(ex.getMessage())
          .build();

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

    List<ValidationError> errors = ex.getBindingResult()
          .getFieldErrors()
          .stream()
          .map(error -> new ValidationError(
                  error.getField(),
                  error.getDefaultMessage()))
          .toList();

    ApiError apiError = ApiError.builder()
          .success(false)
          .message("Validation failed.")
          .errors(errors)
          .build();

    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {

    ApiError error = ApiError.builder()
          .success(false)
          .message("Invalid email or password.")
          .build();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(error);

  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {

    ApiError error = ApiError.builder()
          .success(false)
          .message(ex.getMessage())
          .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(error);
  }
}