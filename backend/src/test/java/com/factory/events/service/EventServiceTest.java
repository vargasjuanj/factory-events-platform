package com.factory.events.service;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.entity.Event;
import com.factory.events.entity.EventType;
import com.factory.events.entity.QualityAudit;
import com.factory.events.exception.DuplicateEventException;
import com.factory.events.mapper.EventMapper;
import com.factory.events.repository.EventRepository;
import com.factory.events.repository.QualityAuditRepository;
import com.factory.events.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 26, 12, 0, 0);

    @Mock
    private EventRepository eventRepository;

    @Mock
    private QualityAuditRepository qualityAuditRepository;

    @Spy
    private EventMapper eventMapper = new EventMapper();

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void registerProductionEvent_Success_ShouldNotCreateAudit() {
        EventRequestDto request = new EventRequestDto("EVT-1001", EventType.PRODUCTION, "Linea de ensamblaje iniciada", NOW);
        Event savedEntity = new Event("EVT-1001", EventType.PRODUCTION, "Linea de ensamblaje iniciada", NOW);

        when(eventRepository.existsByEventId("EVT-1001")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEntity);

        EventResponseDto response = eventService.registerEvent(request);

        assertEquals("EVT-1001", response.eventId());
        assertEquals(EventType.PRODUCTION, response.eventType());
        verify(eventRepository).save(any(Event.class));
        verify(qualityAuditRepository, never()).save(any(QualityAudit.class));
    }

    @Test
    void registerQualityEvent_Success_ShouldCreateAudit() {
        EventRequestDto request = new EventRequestDto("EVT-2002", EventType.QUALITY, "Calibracion fuera de tolerancia", NOW);
        Event savedEntity = new Event("EVT-2002", EventType.QUALITY, "Calibracion fuera de tolerancia", NOW);

        when(eventRepository.existsByEventId("EVT-2002")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEntity);

        EventResponseDto response = eventService.registerEvent(request);

        assertEquals("EVT-2002", response.eventId());
        assertEquals(EventType.QUALITY, response.eventType());
        verify(eventRepository).save(any(Event.class));
        verify(qualityAuditRepository).save(any(QualityAudit.class));
    }

    @Test
    void registerDuplicateEvent_ShouldThrowDuplicateEventException() {
        EventRequestDto request = new EventRequestDto("EVT-9999", EventType.PRODUCTION, "Intento de duplicacion", NOW);

        when(eventRepository.existsByEventId("EVT-9999")).thenReturn(true);

        assertThrows(DuplicateEventException.class, () -> eventService.registerEvent(request));
        verify(eventRepository, never()).save(any(Event.class));
        verify(qualityAuditRepository, never()).save(any(QualityAudit.class));
    }
}
