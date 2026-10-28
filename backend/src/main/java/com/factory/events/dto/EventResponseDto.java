package com.factory.events.dto;

import com.factory.events.entity.EventType;

import java.time.LocalDateTime;

public record EventResponseDto(
        Long id,
        String eventId,
        EventType eventType,
        String description,
        LocalDateTime timestamp,
        LocalDateTime createdAt
) {}
