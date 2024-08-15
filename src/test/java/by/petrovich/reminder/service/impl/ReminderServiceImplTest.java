package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.exception.ReminderNotFoundException;
import by.petrovich.reminder.mapper.ReminderMapper;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.repository.ReminderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.petrovich.reminder.constant.Constant.FORMAT_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ReminderServiceImplTest {
    @InjectMocks
    private ReminderServiceImpl reminderService;

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private ReminderMapper reminderMapper;

    @Test
    void givenPageable_whenFindAll_thenReturnPageOfReminderResponseDtos() {
        // Constants for paging
        int pageNumber = 0;
        int pageSize = 2;

        // Given
        Reminder reminder1 = buildReminder(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);
        Reminder reminder2 = buildReminder(2L, "Dentist", "Checkup appointment", "2024-07-08T11:30:00", 2L);
        List<Reminder> reminders = Arrays.asList(reminder1, reminder2);
        Page<Reminder> reminderPage = new PageImpl<>(reminders, PageRequest.of(pageNumber, pageSize), 2);

        ReminderResponseDto reminderResponseDto1 = buildReminderResponseDto(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);
        ReminderResponseDto reminderResponseDto2 = buildReminderResponseDto(2L, "Dentist", "Checkup appointment", "2024-07-08T11:30:00", 2L);
        List<ReminderResponseDto> expected = Arrays.asList(reminderResponseDto1, reminderResponseDto2);

        when(reminderRepository.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(reminderPage);
        when(reminderMapper.toResponseDto(reminder1)).thenReturn(reminderResponseDto1);
        when(reminderMapper.toResponseDto(reminder2)).thenReturn(reminderResponseDto2);

        // When
        Page<ReminderResponseDto> actual = reminderService.findAll(PageRequest.of(pageNumber, pageSize));

        // Then
        assertNotNull(actual);
        assertEquals(2, actual.getTotalElements());
        assertEquals(2, actual.getNumberOfElements());
        assertEquals(1, actual.getTotalPages());
        assertEquals(0, actual.getNumber());
        verify(reminderRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize));
        assertEquals(expected, actual.getContent());
    }

    @Test
    void givenSort_whenFindAll_thenReturnReminderResponseDtosSortedByTitle() {
        // Given
        Reminder reminder2 = buildReminder(2L, "Zoo Visit", "Take the kids to the zoo on Saturday afternoon.",
                "2024-07-08T11:30:00", 2L);
        Reminder reminder1 = buildReminder(1L, "Annual Report", "Prepare the annual financial report for the board meeting.",
                "2024-07-10T14:30:00", 1L);
        List<Reminder> reminders = Arrays.asList(reminder1, reminder2);
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        ReminderResponseDto reminderResponseDto1 = buildReminderResponseDto(1L, "Annual Report",
                "Prepare the annual financial report for the board meeting.", "2024-07-10T14:30:00", 1L);
        ReminderResponseDto reminderResponseDto2 = buildReminderResponseDto(2L, "Zoo Visit",
                "Take the kids to the zoo on Saturday afternoon.", "2024-07-08T11:30:00", 2L);
        List<ReminderResponseDto> expected = Arrays.asList(reminderResponseDto1, reminderResponseDto2);

        when(reminderRepository.findAll(sort)).thenReturn(reminders);
        when(reminderMapper.toResponseDto(reminder1)).thenReturn(reminderResponseDto1);
        when(reminderMapper.toResponseDto(reminder2)).thenReturn(reminderResponseDto2);

        // When
        List<ReminderResponseDto> actual = reminderService.findAll(sort);

        // Then
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        verify(reminderRepository, times(1)).findAll(sort);
        assertEquals(expected, actual);
    }

    @Test
    void givenReminderId_whenFind_thenReturnReminderResponseDto() {
        // Given
        long id = 1;

        ReminderResponseDto expected = buildReminderResponseDto(1L, "Meeting", "Discuss project plans",
                "2024-07-10T14:30:00", 1L);

        Reminder reminder = buildReminder(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);

        when(reminderRepository.findById(id)).thenReturn(Optional.ofNullable(reminder));
        when(reminderMapper.toResponseDto(reminder)).thenReturn(expected);

        // When
        ReminderResponseDto actual = reminderService.find(id);

        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void givenNonExistentReminderId_whenFind_ThrowsReminderNotFoundException() {
        // Given
        Long id = 999L;
        when(reminderRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        ReminderNotFoundException thrown = assertThrows(
                ReminderNotFoundException.class,
                () -> reminderService.find(id),
                "Expected find() to throw, but it didn't"
        );
        assertEquals("Reminder not found", thrown.getMessage());
    }

    @Test
    void givenReminderRequestDto_whenCreate_thenReturnSavedReminderResponseDto() {
        // Given
        ReminderRequestDto reminderRequestDto = buildReminderRequestDto("Meeting", "Discuss project plans",
                "2024-07-10T14:30:00");

        ReminderResponseDto expected = buildReminderResponseDto(1L, "Meeting", "Discuss project plans",
                "2024-07-10T14:30:00", 1L);

        Reminder reminder = buildReminder(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);

        when(reminderMapper.toEntity(reminderRequestDto)).thenReturn(reminder);
        when(reminderRepository.save(reminder)).thenReturn(reminder);
        when(reminderMapper.toResponseDto(reminder)).thenReturn(expected);

        // When
        ReminderResponseDto actual = reminderService.create(reminderRequestDto);

        // Then
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    void givenExistingReminderId_whenDelete_thenReminderIsDeleted() {
        // Given
        long id = 1L;
        when(reminderRepository.existsById(id)).thenReturn(true);

        // When
        reminderService.delete(id);

        // Then
        verify(reminderRepository, times(1)).deleteById(id);
    }

    @Test
    void givenNonExistentReminderId_whenDelete_thenThrowReminderNotFoundException() {
        // Given
        long id = 999L;
        when(reminderRepository.existsById(id)).thenReturn(false);

        // When & Then
        ReminderNotFoundException thrown = assertThrows(
                ReminderNotFoundException.class,
                () -> reminderService.delete(id),
                "Expected delete() to throw, but it didn't"
        );
        assertEquals("Reminder not found", thrown.getMessage());
        verify(reminderRepository, times(0)).deleteById(id);
    }

    @Test
    void givenIdExistingReminder_whenUpdate_thenReminderResponseDto() {
        // Given
        long id = 1L;
        ReminderRequestDto reminderRequestDto = buildReminderRequestDto("Book Flight", "Travel to Rome",
                "2024-12-11T22:00:00");

        Reminder reminder = buildReminder(1L, "Book Flight", "Travel to London", "2024-10-01T16:00:00", 1L);
        Reminder reminderUpdated = buildReminder(1L, "Book Flight", "Travel to Rome", "2024-12-11T22:00:00", 1L);
        Reminder reminderSaved = buildReminder(1L, "Book Flight", "Travel to Rome", "2024-12-11T22:00:00", 1L);

        ReminderResponseDto expected = buildReminderResponseDto(1L, "Book Flight", "Travel to Rome", "2024-12-11T22:00:00", 1L);

        when(reminderRepository.findById(id)).thenReturn(Optional.ofNullable(reminder));
        when(reminderMapper.toEntityUpdate(reminderRequestDto, reminder)).thenReturn(reminderUpdated);
        when(reminderRepository.save(reminderUpdated)).thenReturn(reminderSaved);
        when(reminderMapper.toResponseDto(reminderSaved)).thenReturn(expected);

        // When
        ReminderResponseDto actual = reminderService.update(id, reminderRequestDto);

        //Then
        assertNotNull(actual);
        InOrder inOrder = inOrder(reminderRepository);
        inOrder.verify(reminderRepository).findById(id);
        inOrder.verify(reminderRepository).save(reminderUpdated);
        assertEquals(actual, expected);
    }

    @Test
    void givenIdExistingReminder_whenUpdate_thenThrowReminderNotFoundException() {
        // Given
        long id = 1L;
        ReminderRequestDto reminderRequestDto = buildReminderRequestDto("Book Flight", "Travel to Rome",
                "2024-12-11T22:00:00");

        when(reminderRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        ReminderNotFoundException thrown = assertThrows(
                ReminderNotFoundException.class,
                () -> reminderService.update(id, reminderRequestDto),
                "Expected update() to throw, but it didn't"
        );
        assertEquals("Reminder not found", thrown.getMessage());
        verify(reminderRepository, times(1)).findById(id);
        verify(reminderRepository, times(0)).save(any(Reminder.class));
    }

    @Test
    void givenTitle_whenFindByTitle_thenReturnReminderResponseDtosSortedByTitle() {
        // Given
        String title = "Birthday";

        Reminder reminder = buildReminder(1L, "Birthday", "Buy a gift for Mary",
                "2024-09-08T10:00:00", 1L);

        List<Reminder> reminders = Collections.singletonList(reminder);

        ReminderResponseDto reminderResponseDto = buildReminderResponseDto(1L, "Birthday", "Buy a gift for Mary",
                "2024-09-08T10:00:00", 1L);
        List<ReminderResponseDto> expected = Collections.singletonList(reminderResponseDto);

        when(reminderRepository.findByTitleContainingIgnoreCase(title)).thenReturn(reminders);
        when(reminderMapper.toResponseDto(reminder)).thenReturn(reminderResponseDto);

        // When
        List<ReminderResponseDto> actual = reminderService.findByTitle(title);

        // Then
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
        verify(reminderRepository, times(1)).findByTitleContainingIgnoreCase(title);
        verify(reminderMapper, times(reminders.size())).toResponseDto(any(Reminder.class));
    }

    @Test
    void givenDescription_whenFindByDescription_thenReturnListOfReminders() {
        // Given
        String description = "project";
        List<Reminder> reminders = Arrays.asList(
                buildReminder(1L, "Meeting", "Discuss project plans for the upcoming project milestone",
                        "2024-07-10T14:30:00", 1L),
                buildReminder(2L, "Meeting", "Discuss project timeline and deliverables",
                        "2024-07-12T10:00:00", 2L)
        );
        List<ReminderResponseDto> expected = Arrays.asList(
                buildReminderResponseDto(1L, "Meeting", "Discuss project plans for the upcoming project milestone",
                        "2024-07-10T14:30:00", 1L),
                buildReminderResponseDto(2L, "Meeting", "Discuss project timeline and deliverables",
                        "2024-07-12T10:00:00", 2L)
        );
        when(reminderMapper.toResponseDto(reminders.get(0))).thenReturn(expected.get(0));
        when(reminderMapper.toResponseDto(reminders.get(1))).thenReturn(expected.get(1));
        when(reminderRepository.findByDescriptionContainingIgnoreCase(description)).thenReturn(reminders);

        // When
        List<ReminderResponseDto> actual = reminderService.findByDescription(description);

        // Then
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(reminderRepository, times(1)).findByDescriptionContainingIgnoreCase(description);
        verify(reminderMapper, times(reminders.size())).toResponseDto(any(Reminder.class));
    }

    @Test
    void givenDate_whenFindByDate_thenReturnListOfReminderResponseDtos() {
        // Given
        String date = "2024-07-10 14:30:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);

        Reminder reminder1 = buildReminder(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);
        Reminder reminder2 = buildReminder(2L, "Checkup", "Doctor appointment", "2024-07-10T14:30:00", 2L);
        List<Reminder> reminders = Arrays.asList(reminder1, reminder2);

        ReminderResponseDto reminderResponseDto1 = buildReminderResponseDto(1L, "Meeting", "Discuss project plans", "2024-07-10T14:30:00", 1L);
        ReminderResponseDto reminderResponseDto2 = buildReminderResponseDto(2L, "Checkup", "Doctor appointment", "2024-07-10T14:30:00", 2L);
        List<ReminderResponseDto> expected = Arrays.asList(reminderResponseDto1, reminderResponseDto2);

        when(reminderMapper.toResponseDto(reminder1)).thenReturn(reminderResponseDto1);
        when(reminderMapper.toResponseDto(reminder2)).thenReturn(reminderResponseDto2);
        when(reminderRepository.findRemindersByRemind(dateTime)).thenReturn(reminders);

        // When
        List<ReminderResponseDto> actual = reminderService.findByDate(date);

        // Then
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(reminderRepository, times(1)).findRemindersByRemind(dateTime);
        verify(reminderMapper, times(reminders.size())).toResponseDto(any(Reminder.class));
    }

    private Reminder buildReminder(long id, String title, String description, String remind, long userId) {
        return Reminder.builder()
                .id(id)
                .title(title)
                .description(description)
                .remind(LocalDateTime.parse(remind))
                .userId(userId)
                .build();
    }

    private ReminderRequestDto buildReminderRequestDto(String title, String description, String remind) {
        return ReminderRequestDto.builder()
                .title(title)
                .description(description)
                .remind(LocalDateTime.parse(remind))
                .build();
    }

    private ReminderResponseDto buildReminderResponseDto(long id, String title, String description, String remind, long userId) {
        return ReminderResponseDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .remind(LocalDateTime.parse(remind))
                .userId(userId)
                .build();
    }
}