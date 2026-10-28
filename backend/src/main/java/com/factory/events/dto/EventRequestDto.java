package com.factory.events.dto;

import com.factory.events.entity.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventRequestDto(
        @NotBlank(message = "El identificador del evento (eventId) es obligatorio.")
        @Pattern(
                regexp = "^EVT-\\d{4}$",
                message = "El eventId debe respetar el formato oficial de planta (ej: EVT-6061, prefijo EVT- seguido de 4 dígitos numéricos)."
        )
        String eventId,

        @NotNull(message = "El tipo de evento es obligatorio.")
        EventType eventType,

        @NotBlank(message = "La descripción del evento es obligatoria.")
        @Size(min = 3, max = 500, message = "La descripción debe tener entre 3 y 500 caracteres.")
        String description,

        @NotNull(message = "La fecha y hora del evento es obligatoria.")
        LocalDateTime timestamp
) {}
