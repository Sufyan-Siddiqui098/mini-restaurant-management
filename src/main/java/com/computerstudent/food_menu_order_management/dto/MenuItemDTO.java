package com.computerstudent.food_menu_order_management.dto;

import com.computerstudent.food_menu_order_management.enums.QuantityUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemDTO {
    @NotEmpty(message = "menu name is required ")
    private String name;
    @NotEmpty(message = "menu description is required ")
    private String description;
    @NotNull(message = "Menu Price is required")
    private BigDecimal price;
    @NotNull(message = "Quantity Type is required e.g. Kilogram, gram, dozen, plate, piece.")
    private QuantityUnit quantityUnit;
    @NotEmpty(message = "Menu tag is required")
    private List<String> tags;
    private boolean isAvailable;

}
