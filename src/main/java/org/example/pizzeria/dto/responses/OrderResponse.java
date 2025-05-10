package org.example.pizzeria.dto.responses;

import java.time.Instant;

public record OrderResponse(
        Long id,
        Long pizzaId,
        String status,
        Instant createdAt
) {
}