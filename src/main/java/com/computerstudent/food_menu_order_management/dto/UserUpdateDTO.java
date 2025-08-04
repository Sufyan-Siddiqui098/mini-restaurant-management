package com.computerstudent.food_menu_order_management.dto;

import com.computerstudent.food_menu_order_management.entity.ChefDetails;
import com.computerstudent.food_menu_order_management.entity.CustomerDetails;
import com.computerstudent.food_menu_order_management.entity.StaffDetails;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDTO {
    private ObjectId id;
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private List<UserRole> roles; // only admin can change


    private ChefDetails chefDetails;
    private CustomerDetails customerDetails;
    private StaffDetails staffDetails;
}

