package org.example.pizzeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pizza_toppings",
            joinColumns = @JoinColumn(name = "pizza_id")
    )
    @Column(name = "topping")
    private Set<String> toppings = new HashSet<>();
}