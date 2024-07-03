package by.petrovich.reminder.service;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.exception.ReminderNotFoundException;

import java.util.List;

public interface ReminderService {
    List<ReminderResponseDto> findAll();

    ReminderResponseDto find(Long id) throws ReminderNotFoundException;

    ReminderResponseDto create(ReminderRequestDto reminderRequestDto);

    void delete(Long id) throws ReminderNotFoundException;

    ReminderResponseDto update(Long id, ReminderRequestDto reminderRequestDto) throws ReminderNotFoundException;

    List<ReminderResponseDto> findByTitle(String title);

    List<ReminderResponseDto> findByDescription(String description);

    List<ReminderResponseDto> findByDate(String date);
}
