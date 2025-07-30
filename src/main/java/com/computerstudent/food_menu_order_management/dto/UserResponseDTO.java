package com.computerstudent.food_menu_order_management.dto;

import com.computerstudent.food_menu_order_management.entity.Address;
import com.computerstudent.food_menu_order_management.entity.ChefDetails;
import com.computerstudent.food_menu_order_management.entity.CustomerDetails;
import com.computerstudent.food_menu_order_management.entity.StaffDetails;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private ObjectId id;
    private String email;
    private String firstName;
    private String lastName;
    private List<UserRole> roles;
    private String phone;

    private ChefDetails chefDetails;
    private CustomerDetails customerDetails;
    private StaffDetails staffDetails;
}
