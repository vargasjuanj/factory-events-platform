package com.factory.events.controller;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.dto.QualityAuditResponseDto;
import com.factory.events.entity.EventType;
import com.factory.events.exception.DuplicateEventException;
import com.factory.events.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 26, 12, 0, 0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    void createEvent_Success_Returns201Created() throws Exception {
        EventRequestDto request = new EventRequestDto("EVT-1001", EventType.PRODUCTION, "Normal operation", NOW);
        EventResponseDto response = new EventResponseDto(1L, "EVT-1001", EventType.PRODUCTION, "Normal operation", NOW, NOW);

        when(eventService.registerEvent(any(EventRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId", is("EVT-1001")))
                .andExpect(jsonPath("$.eventType", is("PRODUCTION")));
    }

    @Test
    void createDuplicateEvent_Returns409Conflict() throws Exception {
        EventRequestDto request = new EventRequestDto("EVT-9999", EventType.QUALITY, "Test duplicado", NOW);

        when(eventService.registerEvent(any(EventRequestDto.class)))
                .thenThrow(new DuplicateEventException("EVT-9999"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.eventId", is("EVT-9999")));
    }

    @Test
    void createEvent_InvalidPayload_Returns400BadRequest() throws Exception {
        EventRequestDto invalidRequest = new EventRequestDto("EVT-INVALID", null, "", null);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors.eventId", notNullValue()))
                .andExpect(jsonPath("$.errors.eventType", notNullValue()))
                .andExpect(jsonPath("$.errors.description", notNullValue()))
                .andExpect(jsonPath("$.errors.timestamp", notNullValue()));
    }

    @Test
    void getAllEvents_Returns200Ok() throws Exception {
        EventResponseDto event = new EventResponseDto(1L, "EVT-1001", EventType.PRODUCTION, "Normal", NOW, NOW);

        when(eventService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventId", is("EVT-1001")));
    }

    @Test
    void getQualityAudits_Returns200Ok() throws Exception {
        QualityAuditResponseDto audit = new QualityAuditResponseDto(1L, "EVT-2002", "Auditoria registrada", NOW);

        when(eventService.getAllQualityAudits()).thenReturn(List.of(audit));

        mockMvc.perform(get("/api/events/audits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventId", is("EVT-2002")));
    }
}
