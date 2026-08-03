package com.teamsync.api.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.teamsync.api.common.response.ApiError;
import com.teamsync.api.common.response.ApiResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleGenericException() {

        Exception exception = new Exception("Something went wrong.");

        ApiResponse<String> response =
                handler.handleException(exception);

        assertFalse(response.isSuccess());
        assertEquals(
                "Something went wrong.",
                response.getMessage());

    }

    @Test
    void shouldHandleBadRequestException() {

        BadRequestException exception =
                new BadRequestException("Invalid request.");

        ResponseEntity<ApiError> response =
                handler.handleBadRequest(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertFalse(response.getBody().isSuccess());

        assertEquals(
                "Invalid request.",
                response.getBody().getMessage());

    }

    @Test
    void shouldHandleBadCredentialsException() {

        BadCredentialsException exception =
                new BadCredentialsException("Bad credentials");

        ResponseEntity<ApiError> response =
                handler.handleBadCredentials(exception);

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                response.getStatusCode());

        assertFalse(response.getBody().isSuccess());

        assertEquals(
                "Invalid email or password.",
                response.getBody().getMessage());

    }

    @Test
    void shouldHandleNotFoundException() {

        NotFoundException exception =
                new NotFoundException("Comment not found.");

        ResponseEntity<ApiError> response =
                handler.handleNotFound(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode());

        assertFalse(response.getBody().isSuccess());

        assertEquals(
                "Comment not found.",
                response.getBody().getMessage());

    }

    @Test
    void shouldHandleValidationException() throws Exception {

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(
                new FieldError(
                        "request",
                        "name",
                        "Name is required."));

        MethodParameter parameter =
                new MethodParameter(
                        DummyController.class.getMethod("dummy", DummyRequest.class),
                        0);

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        parameter,
                        bindingResult);

        ResponseEntity<ApiError> response =
                handler.handleValidation(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertFalse(response.getBody().isSuccess());

        assertEquals(
                "Validation failed.",
                response.getBody().getMessage());

        assertEquals(
                1,
                response.getBody().getErrors().size());

        assertEquals(
        "name",
        response.getBody().getErrors().get(0).getField());

        assertEquals(
                "Name is required.",
                response.getBody().getErrors().get(0).getMessage());

            }

    static class DummyController {

        public void dummy(DummyRequest request) {
        }

    }

    static class DummyRequest {
    }

}
