package com.computerstudent.food_menu_order_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateDTO extends PasswordUpdateByAdminDTO {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

}
