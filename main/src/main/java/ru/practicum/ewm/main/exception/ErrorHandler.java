package ru.practicum.ewm.main.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(final ValidationException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotSavedException(final NotSavedException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getStackTrace(),
                e.getMessage(),
                "Incorrectly made request.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }
}
