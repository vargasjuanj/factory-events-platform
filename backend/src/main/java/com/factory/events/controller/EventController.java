package com.factory.events.controller;

import com.factory.events.dto.EventRequestDto;
import com.factory.events.dto.EventResponseDto;
import com.factory.events.dto.QualityAuditResponseDto;
import com.factory.events.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.registerEvent(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/audits")
    public ResponseEntity<List<QualityAuditResponseDto>> getQualityAudits() {
        return ResponseEntity.ok(eventService.getAllQualityAudits());
    }
}
