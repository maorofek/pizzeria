package org.example.pizzeria.assembler;

import org.example.pizzeria.model.Order;
import org.example.pizzeria.dto.responses.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class OrderAssembler {

    public ResponseEntity<OrderResponse> toResponseEntity(Order order) {
        OrderResponse dto = new OrderResponse(
                order.getId(),
                order.getPizza().getId(),
                order.getStatus().name(),
                order.getCreatedAt()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(dto);
    }
}