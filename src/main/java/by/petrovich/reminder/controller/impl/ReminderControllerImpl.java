package by.petrovich.reminder.controller.impl;

import by.petrovich.reminder.controller.ReminderController;
import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.exception.ReminderNotFoundException;
import by.petrovich.reminder.service.impl.ReminderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderControllerImpl implements ReminderController {
    private static final Logger logger = LoggerFactory.getLogger(ReminderControllerImpl.class);
    private final ReminderServiceImpl reminderService;

    @Override
    @GetMapping
    public ResponseEntity<Page<ReminderResponseDto>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "3") int size) {
        if (page < 0 || size <= 0) {
            logger.error("Invalid page {} or size value: {}", page, size);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.status(OK).body(reminderService.findAll(pageable));
        } catch (Exception e) {
            logger.error("Unexpected error occurred while finding all reminders", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @GetMapping("/sort")
    public ResponseEntity<List<ReminderResponseDto>> findAll(@RequestParam(defaultValue = "ASC") String sortDirection,
                                                             @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            return ResponseEntity.status(OK).body(reminderService.findAll(sort));
        } catch (Exception e) {
            logger.error("Unexpected error occurred while finding all reminders", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> find(@PathVariable Long id) {
        if (id <= 0) {
            logger.error("Invalid id supplied: {}", id);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        try {
            ReminderResponseDto reminderResponseDto = reminderService.find(id);
            return ResponseEntity.status(OK).body(reminderResponseDto);
        } catch (ReminderNotFoundException e) {
            logger.error("Reminder not found with id: {}", id);
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while finding reminder with id: {}", id, e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<ReminderResponseDto> create(@RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        try {
            return ResponseEntity.status(CREATED).body(reminderService.create(reminderRequestDto));
        } catch (Exception e) {
            logger.error("Error occurred while creating a reminder", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            reminderService.delete(id);
            return ResponseEntity.status(NO_CONTENT).build();
        } catch (ReminderNotFoundException e) {
            logger.error("Reminder not found with id: {}", id, e);
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error occurred while deleting a reminder", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> update(@PathVariable Long id,
                                                      @RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        try {
            return ResponseEntity.status(OK).body(reminderService.update(id, reminderRequestDto));
        } catch (ReminderNotFoundException e) {
            logger.error("Reminder not found with id: {}", id, e);
            return ResponseEntity.status(NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error occurred while updating a reminder", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<List<ReminderResponseDto>> searchByCriteria(@RequestParam(value = "title", required = false) String title,
                                                                      @RequestParam(value = "description", required = false) String description,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                      @RequestParam(value = "date", required = false) String date) {
        if (title != null) {
            return ResponseEntity.status(OK).body(reminderService.findByTitle(title));
        } else if (description != null) {
            return ResponseEntity.status(OK).body(reminderService.findByDescription(description));
        } else if (date != null) {
            return ResponseEntity.status(OK).body(reminderService.findByDate(date));
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

}
