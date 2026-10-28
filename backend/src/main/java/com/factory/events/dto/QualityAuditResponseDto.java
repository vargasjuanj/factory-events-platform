package com.factory.events.dto;

import java.time.LocalDateTime;

public record QualityAuditResponseDto(
        Long id,
        String eventId,
        String description,
        LocalDateTime auditTimestamp
) {}
