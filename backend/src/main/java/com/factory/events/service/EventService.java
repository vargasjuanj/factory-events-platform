package com.factory.events.service;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.dto.QualityAuditResponseDto;

import java.util.List;

public interface EventService {

    EventResponseDto registerEvent(EventRequestDto request);

    List<EventResponseDto> getAllEvents();

    List<QualityAuditResponseDto> getAllQualityAudits();
}
