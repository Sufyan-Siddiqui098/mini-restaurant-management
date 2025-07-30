package com.computerstudent.food_menu_order_management.entity;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CustomerDetails {
    @Valid
    private Address address;
}
