package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.example.model.NyPizza.Size.LARGE;
import static org.example.model.Pizza.Topping.HAM;
import static org.example.model.Pizza.Topping.MEAT;
import static org.example.model.Pizza.Topping.MUSHROOM;
import static org.example.model.Pizza.Topping.ONION;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NyPizzaTest {

    @Test
    void nyPizzaPropertiesAreImmutable() {
        var pizza = new NyPizza.Builder(LARGE).addTopping(MEAT).build();
        var toppings = pizza.getToppings();
        toppings.add(ONION);
        assertEquals(Set.of(MEAT), pizza.getToppings(), "NyPizza is not immutable, altering getTopics() return type");
    }

    @Test
    void nyPizzaIsImmutableAfterBuild() {
        var builder = new NyPizza.Builder(LARGE).addTopping(MUSHROOM);
        var pizza = builder.build();
        builder.addTopping(HAM);
        assertEquals(Set.of(MUSHROOM), pizza.getToppings());
    }
}