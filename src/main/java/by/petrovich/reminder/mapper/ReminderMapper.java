package by.petrovich.reminder.mapper;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.model.Reminder;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReminderMapper {
    ReminderResponseDto toResponseDto(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Reminder toEntity(ReminderRequestDto reminderRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Reminder toEntityUpdate(ReminderRequestDto reminderRequestDto, @MappingTarget Reminder reminder);

}
