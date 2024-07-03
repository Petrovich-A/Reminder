package by.petrovich.reminder.controller;

import by.petrovich.reminder.dto.request.ReminderRequestDto;
import by.petrovich.reminder.dto.response.ReminderResponseDto;
import by.petrovich.reminder.model.Reminder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Reminder", description = "API for managing reminders")

public interface ReminderController {

    @Operation(summary = "Find all reminders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all reminders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReminderResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<List<ReminderResponseDto>> findAll();

    @Operation(summary = "Find a reminder by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reminder",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reminder.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Reminder not found",
                    content = @Content)
    })
    ResponseEntity<ReminderResponseDto> find(@Parameter(description = "id of reminder to be searched", example = "1") Long id);

    @Operation(summary = "Create a new reminder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reminder created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReminderResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    ResponseEntity<ReminderResponseDto> create(
            @Parameter(description = "Reminder to be created", example = "{\"title\": \"titleCreate\", \"description\": \"descriptionCreate\", \"userId\": 999}")
            ReminderRequestDto reminderRequestDto);

    @Operation(summary = "Delete a reminder by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reminder deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Reminder not found",
                    content = @Content)
    })
    ResponseEntity<Void> delete(@Parameter(description = "id of reminder to be deleted", example = "999") Long id);

    @Operation(summary = "Update a reminder by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReminderResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Reminder not found",
                    content = @Content)
    })
    ResponseEntity<ReminderResponseDto> update(@Parameter(description = "id of reminder to be updated", example = "999")
                                               Long id,
                                               @Parameter(description = "Updated reminder data", example = "{\"title\": \"titleUpdate\", \"description\": \"descriptionUpdate\", \"userId\": 999}")
                                               ReminderRequestDto reminderRequestDto);

    @Operation(summary = "Search reminders by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found reminders matching criteria",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReminderResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No reminders found",
                    content = @Content)
    })
    ResponseEntity<List<ReminderResponseDto>> searchByCriteria(
            @Parameter(description = "Title of the reminder", example = "de") String title,
            @Parameter(description = "Description of the reminder", example = "Buy a gift") String description,
            @Parameter(description = "Date of the reminder", example = "2023-05-02 11:30:00") String date);
}
