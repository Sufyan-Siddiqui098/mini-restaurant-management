package com.computerstudent.food_menu_order_management.entity;

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
    @NotNull(message = "Menu name is required ")
    private String name;
    @NotNull(message = "Menu description is required ")
    private String description;
    @NotNull(message = "Menu Price is required")
    private BigDecimal price;
    private List<String> tags;
    private boolean isAvailable;

    @DBRef
    private User chef;
}
