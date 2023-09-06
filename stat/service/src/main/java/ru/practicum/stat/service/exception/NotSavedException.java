package ru.practicum.stat.service.exception;

public class NotSavedException extends RuntimeException {
    public NotSavedException(String message) {
        super(message);
    }
}
