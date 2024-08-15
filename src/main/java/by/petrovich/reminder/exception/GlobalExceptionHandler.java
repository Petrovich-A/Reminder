package by.petrovich.reminder.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ReminderErrorResponse> handleException(Exception e) {
        logger.error("Unexpected error occurred", e);
        ReminderErrorResponse reminderErrorResponse = new ReminderErrorResponse(INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON).body(reminderErrorResponse);
    }

    @ExceptionHandler(ReminderNotFoundException.class)
    public ResponseEntity<ReminderErrorResponse> handleReminderNotFoundException(ReminderNotFoundException e) {
        logger.error("Reminder not found", e);
        ReminderErrorResponse reminderErrorResponse = new ReminderErrorResponse(NOT_FOUND.value(), "Not Found", e.getMessage());
        return ResponseEntity.status(NOT_FOUND).contentType(APPLICATION_JSON).body(reminderErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ReminderErrorResponse> handleArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        logger.error("Invalid argument", e);
        ReminderErrorResponse reminderErrorResponse = new ReminderErrorResponse(BAD_REQUEST.value(), "Bad Request", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(reminderErrorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ReminderErrorResponse> handleValidationExceptions(ConstraintViolationException e) {
        logger.error("find.id: received negative or invalid id in url", e);
        ReminderErrorResponse reminderErrorResponse = new ReminderErrorResponse(BAD_REQUEST.value(), "Bad Request", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(reminderErrorResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ReminderErrorResponse> handleNumberFormatExceptionExceptions(NumberFormatException e) {
        logger.error("Invalid ID format", e);
        ReminderErrorResponse reminderErrorResponse = new ReminderErrorResponse(BAD_REQUEST.value(), "Bad Request", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(reminderErrorResponse);
    }
}
