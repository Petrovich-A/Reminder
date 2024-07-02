package by.petrovich.reminder.mapper;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.model.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReminderMapper {
    ReminderResponseDto toResponseDto(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "remind", expression = "java(java.time.LocalDateTime.now())")
    Reminder toEntity(ReminderRequestDto reminderRequestDto);

    @Mapping(target = "id", ignore = true)
    Reminder toEntityUpdate(ReminderRequestDto reminderRequestDto, @MappingTarget Reminder reminder);

}
