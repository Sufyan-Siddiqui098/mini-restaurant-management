package com.computerstudent.food_menu_order_management.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum QuantityUnit {
    PLATE,
    DOZEN,
    GRAM,
    PIECE,
    KILOGRAM;

    @JsonCreator
    public static QuantityUnit fromString(String key) {
        String quantityUnits = String
                .join(", ", Arrays.stream(QuantityUnit.values())
                .map(Enum::name)
                .toArray(String[]::new));

        return Arrays.stream(QuantityUnit.values())
                .filter(quantityUnit -> quantityUnit.name().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("'%s' is not a valid quantity unit. Allowed values are: %s.", key, quantityUnits)
                ));
    }
}
