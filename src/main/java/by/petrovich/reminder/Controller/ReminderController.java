package by.petrovich.reminder.Controller;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.service.impl.ReminderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderServiceImpl reminderService;

    @GetMapping
    public ResponseEntity<List<ReminderResponseDto>> findAll() {
        return ResponseEntity.status(OK).body(reminderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> find(@PathVariable Long id) {
        Optional<Reminder> optionalReminder = reminderService.find(id);
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            System.out.println("Reminder found: " + reminder);
            return ResponseEntity.status(OK).body(reminder);
        } else {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ReminderResponseDto> create(@RequestBody ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reminderService.create(reminderRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reminderService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> update(@PathVariable Long id,
                                                      @RequestBody ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.status(OK).body(reminderService.update(id, reminderRequestDto));
    }

}
