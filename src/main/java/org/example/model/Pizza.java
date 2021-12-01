package org.example.model;

import java.util.EnumSet;
import java.util.Set;

public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE, MEAT }

    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {

        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(topping);
            return self();
        }

        protected abstract T self();

        abstract Pizza build();

    }

    protected Pizza(Builder<?> builder) {
        toppings = builder.toppings;
    }

    Set<Topping> getToppings() {
        return toppings;
    }
}
