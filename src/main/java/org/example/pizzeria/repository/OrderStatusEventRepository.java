package org.example.pizzeria.repository;

import org.example.pizzeria.model.OrderStatusEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusEventRepository
        extends JpaRepository<OrderStatusEvent, Long> {

}