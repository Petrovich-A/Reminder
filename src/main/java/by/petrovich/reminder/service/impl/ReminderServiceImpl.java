package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.constant.Constant;
import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.exception.ReminderNotFoundException;
import by.petrovich.reminder.mapper.ReminderMapper;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.petrovich.reminder.constant.Constant.FORMAT_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    @Override
    public Page<ReminderResponseDto> findAll(Pageable pageable) {
        Page<Reminder> reminders = reminderRepository.findAll(pageable);
        return reminders.map(reminderMapper::toResponseDto);
    }

    @Override
    public List<ReminderResponseDto> findAll(Sort sort) {
        List<Reminder> reminders = reminderRepository.findAll(sort);
        return reminders.stream().map(reminderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReminderResponseDto find(Long id) {
        return reminderMapper.toResponseDto(reminderRepository.findById(id)
                .orElseThrow(() -> new ReminderNotFoundException(id)));
    }

    @Override
    @Transactional
    public ReminderResponseDto create(ReminderRequestDto reminderRequestDto) {
        Reminder saved = reminderRepository.save(reminderMapper.toEntity(reminderRequestDto));
        return reminderMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id);
        } else {
            throw new ReminderNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public ReminderResponseDto update(Long id, ReminderRequestDto reminderRequestDto) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isEmpty()) {
            throw new ReminderNotFoundException(id);
        } else {
            Reminder reminderUpdated = reminderMapper.toEntityUpdate(reminderRequestDto, optionalReminder.get());
            Reminder saved = reminderRepository.save(reminderUpdated);
            return reminderMapper.toResponseDto(saved);
        }
    }

    @Override
    public List<ReminderResponseDto> findByTitle(String title) {
        List<Reminder> reminders = reminderRepository.findByTitleContainingIgnoreCase(title);
        return reminders.stream()
                .map(reminderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDto> findByDescription(String description) {
        List<Reminder> reminders = reminderRepository.findByDescriptionContainingIgnoreCase(description);
        return reminders.stream()
                .map(reminderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDto> findByDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);
        List<Reminder> reminders = reminderRepository.findRemindersByRemind(dateTime);
        return reminders.stream()
                .map(reminderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
