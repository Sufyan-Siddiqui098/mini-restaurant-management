package com.computerstudent.food_menu_order_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemDTO {
    @NotEmpty(message = "menu name is required ")
    private String name;
    @NotEmpty(message = "menu description is required ")
    private String description;
    @NotEmpty(message = "Menu Price is required")
    private BigDecimal price;
    @NotEmpty(message = "Menu tag is required")
    private List<String> tags;
    private boolean isAvailable;

}
