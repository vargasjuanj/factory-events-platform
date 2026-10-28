package com.factory.events.mapper;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.dto.QualityAuditResponseDto;
import com.factory.events.entity.Event;
import com.factory.events.entity.QualityAudit;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequestDto dto) {
        return new Event(
                dto.eventId().trim(),
                dto.eventType(),
                dto.description().trim(),
                dto.timestamp()
        );
    }

    public EventResponseDto toResponseDto(Event entity) {
        return new EventResponseDto(
                entity.getId(),
                entity.getEventId(),
                entity.getEventType(),
                entity.getDescription(),
                entity.getTimestamp(),
                entity.getCreatedAt()
        );
    }

    public QualityAuditResponseDto toAuditResponseDto(QualityAudit entity) {
        return new QualityAuditResponseDto(
                entity.getId(),
                entity.getEventId(),
                entity.getDescription(),
                entity.getAuditTimestamp()
        );
    }
}
