package com.computerstudent.food_menu_order_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateDTO {

    @NotEmpty(message = "Old password is required")
    private String oldPassword;
    @NotEmpty(message = "New password is required")
    private String newPassword;
    @NotEmpty(message = "Confirm New password is required")
    private String confirmNewPassword;
}
