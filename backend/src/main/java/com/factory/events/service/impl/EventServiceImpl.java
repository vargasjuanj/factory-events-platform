package com.factory.events.service.impl;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.dto.QualityAuditResponseDto;
import com.factory.events.entity.Event;
import com.factory.events.entity.EventType;
import com.factory.events.entity.QualityAudit;
import com.factory.events.exception.DuplicateEventException;
import com.factory.events.mapper.EventMapper;
import com.factory.events.repository.EventRepository;
import com.factory.events.repository.QualityAuditRepository;
import com.factory.events.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final QualityAuditRepository qualityAuditRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository,
                            QualityAuditRepository qualityAuditRepository,
                            EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.qualityAuditRepository = qualityAuditRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    public EventResponseDto registerEvent(EventRequestDto request) {
        final String eventId = request.eventId().trim();
        log.info("Procesando evento [eventId: {}, tipo: {}]", eventId, request.eventType());

        if (eventRepository.existsByEventId(eventId)) {
            log.warn("Colisión de eventId duplicado: {}", eventId);
            throw new DuplicateEventException(eventId);
        }

        Event savedEvent = eventRepository.save(eventMapper.toEntity(request));

        if (savedEvent.getEventType() == EventType.QUALITY) {
            qualityAuditRepository.save(new QualityAudit(savedEvent.getEventId(), savedEvent.getDescription()));
        }

        return eventMapper.toResponseDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAllByOrderByTimestampDesc()
                .stream()
                .map(eventMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityAuditResponseDto> getAllQualityAudits() {
        return qualityAuditRepository.findAllByOrderByAuditTimestampDesc()
                .stream()
                .map(eventMapper::toAuditResponseDto)
                .toList();
    }
}
