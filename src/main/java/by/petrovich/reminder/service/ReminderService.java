package by.petrovich.reminder.service;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.exception.ReminderNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReminderService {
    Page<ReminderResponseDto> findAll(Pageable pageable);

    List<ReminderResponseDto> findAll(Sort sort);

    ReminderResponseDto find(Long id) throws ReminderNotFoundException;

    ReminderResponseDto create(ReminderRequestDto reminderRequestDto);

    void delete(Long id) throws ReminderNotFoundException;

    ReminderResponseDto update(Long id, ReminderRequestDto reminderRequestDto) throws ReminderNotFoundException;

    List<ReminderResponseDto> searchByCriteria(String title, String description, String date);
}
