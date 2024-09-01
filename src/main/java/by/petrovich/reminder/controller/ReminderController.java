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

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderServiceImpl reminderService;

    @GetMapping
    public ResponseEntity<Page<ReminderResponseDto>> findAll(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                             @RequestParam(name = "size", defaultValue = "3") @Min(0) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(reminderService.findAll(pageable));
    }

    @GetMapping("/sort")
    public ResponseEntity<List<ReminderResponseDto>> findAllSorted(@RequestParam(defaultValue = "ASC") String sortDirection,
                                                                   @RequestParam(defaultValue = "id") String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return ResponseEntity.ok().body(reminderService.findAll(sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> find(@PathVariable @Positive Long id) {
        ReminderResponseDto reminderResponseDto = reminderService.find(id);
        return ResponseEntity.ok().body(reminderResponseDto);
    }

    @PostMapping("/")
    public ResponseEntity<ReminderResponseDto> create(@RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.status(CREATED).body(reminderService.create(reminderRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @Positive Long id) {
        reminderService.delete(id);
        return ResponseEntity.ok("Reminder deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> update(@PathVariable @Positive Long id,
                                                      @RequestBody @Valid ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.ok().body(reminderService.update(id, reminderRequestDto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReminderResponseDto>> searchByCriteria(@RequestParam(value = "title", required = false) String title,
                                                                      @RequestParam(value = "description", required = false) String description,
                                                                      @RequestParam(value = "date", required = false) String date) {
        return ResponseEntity.ok().body(reminderService.searchByCriteria(title, description, date));
    }

}
