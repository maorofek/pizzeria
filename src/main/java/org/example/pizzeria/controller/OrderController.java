package org.example.pizzeria.controller;


import jakarta.validation.Valid;
import org.example.pizzeria.dto.requests.CreateOrderRequest;
import org.example.pizzeria.assembler.OrderAssembler;
import org.example.pizzeria.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final OrderAssembler assembler;

    public OrderController(OrderService orderService,
                           OrderAssembler assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateOrderRequest req) {
        var order = orderService.createOrder(req.pizzaId());
        return assembler.toResponseEntity(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return orderService.findById(id)
                .map(assembler::toResponseEntity)
                .orElse(ResponseEntity.notFound().build());
    }
}