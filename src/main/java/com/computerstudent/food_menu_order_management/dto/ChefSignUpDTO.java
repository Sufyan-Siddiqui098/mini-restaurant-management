package com.computerstudent.food_menu_order_management.dto;

import com.computerstudent.food_menu_order_management.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChefSignUpDTO extends CustomerSignUpDTO {
    @NotEmpty(message = "At least one specialization is required")
    private List<String> specialization;
}
