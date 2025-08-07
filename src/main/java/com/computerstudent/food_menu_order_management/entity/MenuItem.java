package com.computerstudent.food_menu_order_management.entity;

import com.computerstudent.food_menu_order_management.enums.QuantityUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "menu_item")
@NoArgsConstructor
public class MenuItem {

    @Id
    private ObjectId id;
    @NotBlank(message = "Menu name is required ")
    private String name;
    @NotBlank(message = "Menu description is required ")
    private String description;
    @NotNull(message = "Menu Price is required")
    private BigDecimal price;
    @NotBlank(message = "Quantity Unit is required e.g. Kilogram, gram, plate, piece ")
    private QuantityUnit quantityUnit;
    private List<String> tags;
    private boolean isAvailable;

    @DBRef
    private User chef;
}
