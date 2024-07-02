package by.petrovich.reminder.service;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.model.Reminder;

import java.util.List;
import java.util.Optional;

public interface ReminderService {
    List<ReminderResponseDto> findAll();

    Optional<Reminder> find(Long id);

    ReminderResponseDto create(ReminderRequestDto reminderRequestDto);

    void delete(Long id);

    ReminderResponseDto update(Long id, ReminderRequestDto reminderRequestDto);

    List<ReminderResponseDto> findByTitle(String title);

    List<ReminderResponseDto> findByDescription(String description);

    List<ReminderResponseDto> findByDate(String date);
}
