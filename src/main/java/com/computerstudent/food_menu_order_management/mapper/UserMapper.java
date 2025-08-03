package com.computerstudent.food_menu_order_management.mapper;

import com.computerstudent.food_menu_order_management.dto.ChefSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.CustomerSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.LoginResponseDTO;
import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.entity.ChefDetails;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles());
        // If admin, set ALL details
        if (user.getRoles().contains(UserRole.ADMIN)) {
            dto.setCustomerDetails(user.getCustomerDetails());
            dto.setChefDetails(user.getChefDetails());
            dto.setStaffDetails(user.getStaffDetails());
        } else {
            // If not admin, set details based on specific roles
            if (user.getRoles().contains(UserRole.CUSTOMER)) {
                dto.setCustomerDetails(user.getCustomerDetails());
            }
            if (user.getRoles().contains(UserRole.CHEF)) {
                dto.setChefDetails(user.getChefDetails());
            }
            if (user.getRoles().contains(UserRole.DELIVERY_STAFF)) {
                dto.setStaffDetails(user.getStaffDetails());
            }
        }
        return dto;
    }

    public User fromCustomerSignupDTOtoUser(CustomerSignUpDTO customer){
        User user = new User();
        user.setEmail(customer.getEmail());
        user.setUserName(customer.getUserName());
        user.setPassword(passwordEncoder.encode(customer.getPassword()));
        user.setPhone(customer.getPhone());
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setRoles(List.of(UserRole.CUSTOMER));


        return user;
    }

    public User fromChefSignupDTOtoUser(ChefSignUpDTO chef){
        User user = new User();
        user.setEmail(chef.getEmail());
        user.setUserName(chef.getUserName());
        user.setPassword(passwordEncoder.encode(chef.getPassword()));
        user.setPhone(chef.getPhone());
        user.setFirstName(chef.getFirstName());
        user.setLastName(chef.getLastName());
        user.setRoles(List.of(UserRole.CHEF));

        if (user.getChefDetails() == null) {
            user.setChefDetails(new ChefDetails());
        }

        user.getChefDetails().setSpecialization(chef.getSpecialization());

        return user;
    }

    public LoginResponseDTO fromUserToLoginResponseDTO(User user, String token){
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setToken(token);

        return dto;

    }
}
