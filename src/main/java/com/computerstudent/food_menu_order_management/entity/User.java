package com.computerstudent.food_menu_order_management.entity;


import com.computerstudent.food_menu_order_management.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "users")
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Password is required")
    private String password;
    private String phone;
    private String firstName;
    private String lastName;
    private List<UserRole> roles;
//    private Address address;

    private CustomerDetails customerDetails;
    private ChefDetails chefDetails;
    private StaffDetails staffDetails;

}
