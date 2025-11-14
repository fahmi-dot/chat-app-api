package com.fahmi.chatappapi.exception;

import com.fahmi.chatappapi.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(CustomException.BadRequestException e) {
        return ResponseUtil.response(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(CustomException.AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(CustomException.AuthenticationException e) {
        return ResponseUtil.response(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(CustomException.AuthorizationException.class)
    public ResponseEntity<?> handleAuthorization(CustomException.AuthorizationException e) {
        return ResponseUtil.response(
                HttpStatus.FORBIDDEN,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(CustomException.ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(CustomException.ResourceNotFoundException e) {
        return ResponseUtil.response(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(CustomException.ConflictException.class)
    public ResponseEntity<?> handleConflict(CustomException.ConflictException e) {
        return ResponseUtil.response(
                HttpStatus.CONFLICT,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseUtil.response(
                HttpStatus.EXPECTATION_FAILED,
                e.getMessage(),
                null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception e) {
        return ResponseUtil.response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred.",
                null);
    }
}
