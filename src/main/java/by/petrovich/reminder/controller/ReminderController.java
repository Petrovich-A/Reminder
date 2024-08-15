package by.petrovich.reminder.controller;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.service.impl.ReminderServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
@Validated
public class ReminderController {
    private final ReminderServiceImpl reminderService;

    @GetMapping
    public ResponseEntity<Page<ReminderResponseDto>> findAll(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                             @RequestParam(name = "size", defaultValue = "1") @Min(0) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(OK).body(reminderService.findAll(pageable));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<ReminderResponseDto>> findAll(@RequestParam(defaultValue = "ASC") String sortDirection,
                                                             @RequestParam(defaultValue = "id") String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return ResponseEntity.status(OK).body(reminderService.findAll(sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> find(@PathVariable @Positive Long id) {
        ReminderResponseDto reminderResponseDto = reminderService.find(id);
        return ResponseEntity.status(OK).body(reminderResponseDto);
    }

    @PostMapping("/")
    public ResponseEntity<ReminderResponseDto> create(@RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.status(CREATED).body(reminderService.create(reminderRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @Positive Long id) {
        reminderService.delete(id);
        return ResponseEntity.status(NO_CONTENT).body("Reminder deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> update(@PathVariable @Positive Long id,
                                                      @RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.status(OK).body(reminderService.update(id, reminderRequestDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ReminderResponseDto>> searchByCriteria(@RequestParam(value = "title", required = false) String title,
                                                                      @RequestParam(value = "description", required = false) String description,
                                                                      @RequestParam(value = "date", required = false) String date) {
        if (title != null) {
            return ResponseEntity.status(OK).body(reminderService.findByTitle(title));
        } else if (description != null) {
            return ResponseEntity.status(OK).body(reminderService.findByDescription(description));
        } else if (date != null) {
            return ResponseEntity.status(OK).body(reminderService.findByDate(date));
        } else {
            throw new IllegalArgumentException("At least one search criteria must be provided");
        }
    }

}
