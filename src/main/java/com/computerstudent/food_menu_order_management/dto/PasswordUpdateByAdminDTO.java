package com.computerstudent.food_menu_order_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateByAdminDTO {
    @NotBlank(message = "New password is required")
    private String newPassword;
    @NotBlank(message = "Confirm New password is required")
    private String confirmNewPassword;
}
