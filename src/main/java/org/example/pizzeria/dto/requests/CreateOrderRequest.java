package org.example.pizzeria.dto.requests;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull(message = "pizzaId is required")
        @Positive(message = "pizzaId must be a positive number")
        Long pizzaId
) {
}