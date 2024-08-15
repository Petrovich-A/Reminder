package by.petrovich.reminder.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ReminderControllerIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.2");
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }

    @Test
    void shouldReturnReminderById_whenGetRequestIsMade() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reminders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Meeting"))
                .andExpect(jsonPath("$.description").value("Discuss project plans"))
                .andExpect(jsonPath("$.remind").value("2024-07-10T14:30:00"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null));
    }

    @Test
    void shouldReturnReminderNotFound_whenGetRequestIsMade() throws Exception {
        int reminderId = 49;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reminders/" + reminderId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Reminder with id " + reminderId + " not found"))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null));
    }

    @Test
    void shouldReturnBadRequest_whenGetRequestIsMadeWithInvalidId() throws Exception {
        int invalidReminderId = -1;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reminders/" + invalidReminderId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("find.id: must be greater than 0"))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null));
    }
}