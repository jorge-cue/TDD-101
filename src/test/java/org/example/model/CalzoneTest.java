package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.example.model.Pizza.Topping.MEAT;
import static org.example.model.Pizza.Topping.PEPPER;
import static org.example.model.Pizza.Topping.SAUSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalzoneTest {

    @Test
    void calzonePropertiesAreImmutable() {
        final var calzone = new Calzone.Builder().addTopping(MEAT).build();
        var toppings = calzone.getToppings();
        toppings.add(PEPPER);
        assertEquals(Set.of(MEAT), calzone.getToppings());
    }

    @Test
    void calzoneIsImmutableAfterBuild() {
        var builder = new Calzone.Builder().addTopping(MEAT);
        var calzone = builder.build();
        builder.addTopping(SAUSAGE);
        assertEquals(Set.of(MEAT), calzone.getToppings());
    }
}