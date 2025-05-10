package org.example.pizzeria.service;


import org.example.pizzeria.model.Pizza;
import org.example.pizzeria.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<Pizza> findAll() {
        return pizzaRepository.findAll();
    }

    public Optional<Pizza> findById(Long id) {
        return pizzaRepository.findById(id);
    }

    public Pizza create(Pizza pizza) {
        // you could add business logic/validation here if needed
        return pizzaRepository.save(pizza);
    }
}