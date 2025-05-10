// src/main/java/org/example/pizzeria/service/OrderListeners.java
package org.example.pizzeria.service;

import org.example.pizzeria.model.Order;
import org.example.pizzeria.model.OrderStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class OrderListeners {

    private final OrderService orderService;
    private final Executor doughExec, toppingExec, ovenExec, waiterExec;

    public OrderListeners(OrderService orderService,
                          @Qualifier("doughChefExecutor") Executor doughExec,
                          @Qualifier("toppingChefExecutor") Executor toppingExec,
                          @Qualifier("ovenExecutor") Executor ovenExec,
                          @Qualifier("waiterExecutor") Executor waiterExec) {
        this.orderService = orderService;
        this.doughExec = doughExec;
        this.toppingExec = toppingExec;
        this.ovenExec = ovenExec;
        this.waiterExec = waiterExec;
    }

    @KafkaListener(topics = "orders-received")
    public void onReceived(Order o) {
        doughExec.execute(() -> {
            logStage("Dough start", o);
            sleepSeconds(7);
            logStage("Dough end", o);
            orderService.advanceStage(o, OrderStatus.IN_OVEN, "orders-in-oven");
        });
    }

    @KafkaListener(topics = "orders-in-oven")
    public void onInOven(Order o) {
        toppingExec.execute(() -> {
            logStage("Topping start", o);
            int count = o.getPizza().getToppings().size();
            sleepSeconds(4 * Math.max(1, count));
            logStage("Topping end", o);
            orderService.advanceStage(o, OrderStatus.READY, "orders-ready");
        });
    }

    @KafkaListener(topics = "orders-ready")
    public void onReady(Order o) {
        ovenExec.execute(() -> {
            logStage("Oven start", o);
            sleepSeconds(10);
            logStage("Oven end", o);
            orderService.advanceStage(o, OrderStatus.OUT_FOR_DELIVERY, "orders-out-for-delivery");
        });
    }

    @KafkaListener(topics = "orders-out-for-delivery")
    public void onOutForDelivery(Order o) {
        waiterExec.execute(() -> {
            logStage("Serving start", o);
            sleepSeconds(5);
            logStage("Serving end", o);
            orderService.advanceStage(o, OrderStatus.COMPLETED, "orders-delivered");
        });
    }

    private void logStage(String msg, Order o) {
        System.out.printf("%s order=%d at %s on thread=%s%n",
                msg, o.getId(), java.time.Instant.now(), Thread.currentThread().getName());
    }

    private void sleepSeconds(int sec) {
        try {
            Thread.sleep(sec * 1_000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
