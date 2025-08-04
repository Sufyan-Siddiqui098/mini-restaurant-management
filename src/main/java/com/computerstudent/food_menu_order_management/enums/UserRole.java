package com.computerstudent.food_menu_order_management.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum UserRole {
    CUSTOMER,
    ADMIN,
    CHEF,
    DELIVERY_STAFF;

    @JsonCreator
    public static UserRole fromString(String key){
        return Arrays.stream(UserRole.values())
                .filter(role -> role.name().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + key));
    }
}
