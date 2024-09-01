package by.petrovich.reminder.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(ReminderNotFoundException.class)
    public ResponseEntity<String> handleReminderNotFoundException(ReminderNotFoundException e) {
        log.error("Error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        log.error("Error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument", e);
        return ResponseEntity.status(BAD_REQUEST).body("Invalid argument");
    }

    @ExceptionHandler(SearchCriteriaException.class)
    public ResponseEntity<String> handleSearchCriteriaException(SearchCriteriaException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidDataTimeSearchCriteriaException.class)
    public ResponseEntity<String> handleInvalidDataTimeSearchCriteriaException(InvalidDataTimeSearchCriteriaException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
    }
}
