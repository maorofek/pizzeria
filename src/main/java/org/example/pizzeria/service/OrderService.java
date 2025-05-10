package org.example.pizzeria.service;


import lombok.extern.slf4j.Slf4j;
import org.example.pizzeria.controller.OrderEventsController;
import org.example.pizzeria.model.Order;
import org.example.pizzeria.model.OrderStatus;
import org.example.pizzeria.model.OrderStatusEvent;
import org.example.pizzeria.model.Pizza;
import org.example.pizzeria.repository.OrderRepository;
import org.example.pizzeria.repository.OrderStatusEventRepository;
import org.example.pizzeria.repository.PizzaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final PizzaRepository pizzaRepo;
    private final OrderStatusEventRepository orderStatusEventRepo;
    private final KafkaTemplate<String, Order> kafka;

    public OrderService(OrderRepository orderRepo,
                        PizzaRepository pizzaRepo, OrderStatusEventRepository orderStatusEventRepo,
                        KafkaTemplate<String, Order> kafka) {
        this.orderRepo = orderRepo;
        this.pizzaRepo = pizzaRepo;
        this.orderStatusEventRepo = orderStatusEventRepo;
        this.kafka = kafka;
    }

    public Order createOrder(Long pizzaId) {
        Pizza pizza = pizzaRepo.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("No pizza with id=" + pizzaId));

        Order o = new Order();
        o.setPizza(pizza);
        o.setStatus(OrderStatus.RECEIVED);
        Order saved = orderRepo.save(o);

        log.info("Order {} status={} at {}",
                saved.getId(), saved.getStatus(), saved.getCreatedAt());

        kafka.send("orders-received", saved);
        return saved;
    }

    public Order advanceStage(Order o, OrderStatus nextStatus, String nextTopic) {
        o.setStatus(nextStatus);
        Instant now = Instant.now();
        switch (nextStatus) {
            case IN_OVEN -> o.setInOvenAt(now);
            case READY -> o.setReadyAt(now);
            case OUT_FOR_DELIVERY -> o.setOutForDeliveryAt(now);
            default -> {
            }
        }
        Order updated = orderRepo.save(o);
        var evt = new OrderStatusEvent(
                null,
                updated,
                nextStatus,
                Instant.now()
        );
        orderStatusEventRepo.save(evt);

        log.info("Order {} status changed to {} at {}",
                updated.getId(), nextStatus, now);

        kafka.send(nextTopic, updated);
        return updated;
    }

    public Optional<Order> findById(Long id) {
        return orderRepo.findById(id);
    }
}
