package com.computerstudent.food_menu_order_management.dto;

import com.computerstudent.food_menu_order_management.config.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemResponseDTO {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;
    private String description;
    private BigDecimal price;
    private List<String> tags;
    private boolean isAvailable;

    private ChefResponseDTO chef;
}
