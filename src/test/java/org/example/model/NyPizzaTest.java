package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NyPizzaTest {

    @Test
    void nyPizzaIsImmutable() {
        assertAll(
                () -> {
                    var pizza = new NyPizza.Builder(NyPizza.Size.LARGE).addTopping(Pizza.Topping.MEAT).build();
                    var toppings = pizza.getToppings();
                    toppings.add(Pizza.Topping.ONION);
                    assertEquals(EnumSet.of(Pizza.Topping.MEAT), pizza.getToppings(), "NyPizza is not immutable, altering getTopics() return type");
                },
                () -> {
                    var builder = new NyPizza.Builder(NyPizza.Size.LARGE);
                    var pizza = builder.addTopping(Pizza.Topping.MEAT).build();
                    builder.addTopping(Pizza.Topping.ONION);
                    assertEquals(EnumSet.of(Pizza.Topping.MEAT), pizza.getToppings(), "NyPizza is not immutable, altering builder");
                }
        );

    }
}