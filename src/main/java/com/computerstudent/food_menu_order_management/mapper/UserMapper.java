package com.computerstudent.food_menu_order_management.mapper;

import com.computerstudent.food_menu_order_management.dto.*;
import com.computerstudent.food_menu_order_management.entity.ChefDetails;
import com.computerstudent.food_menu_order_management.entity.CustomerDetails;
import com.computerstudent.food_menu_order_management.entity.StaffDetails;
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

    public User fromCustomerSignupDTOtoUser(CustomerSignUpDTO customer) {
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

    public User fromChefSignupDTOtoUser(ChefSignUpDTO chef) {
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

    public LoginResponseDTO fromUserToLoginResponseDTO(User user, String token) {
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

    public void updateUserFromDTO(UserUpdateDTO dto, User currentUser, User userInDb) {
        boolean isSelf = currentUser.getId().equals(userInDb.getId());
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);

        if (dto.getFirstName() != null) userInDb.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) userInDb.setLastName(dto.getLastName());
        if (dto.getPhone() != null) userInDb.setPhone(dto.getPhone());

        if (isAdmin) {
            if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
                if (userInDb.getRoles().contains(UserRole.ADMIN) && !dto.getRoles().contains(UserRole.ADMIN)) {
                    throw new UnsupportedOperationException("Cannot remove ADMIN role from the user.");
                }
                userInDb.setRoles(dto.getRoles());
                if (!dto.getRoles().contains(UserRole.CHEF)) userInDb.setChefDetails(null);
                if (!dto.getRoles().contains(UserRole.DELIVERY_STAFF)) userInDb.setStaffDetails(null);
            }
            if (dto.getChefDetails() != null)
                userInDb.setChefDetails(dto.getChefDetails());
            if (dto.getCustomerDetails() != null)
                userInDb.setCustomerDetails(dto.getCustomerDetails());
            if (dto.getStaffDetails() != null)
                userInDb.setStaffDetails(dto.getStaffDetails());

        } else if (isSelf) {
            // Update Chef Details (Allowed Fields only)
            if (userInDb.getRoles().contains(UserRole.CHEF)) {
                ChefDetails updatedChefDetails = userInDb.getChefDetails();

                if (updatedChefDetails == null)
                    updatedChefDetails = new ChefDetails();
                if (dto.getChefDetails() != null && dto.getChefDetails().getSpecialization() != null)
                    updatedChefDetails.setSpecialization(dto.getChefDetails().getSpecialization());

                userInDb.setChefDetails(updatedChefDetails);
            }
            // -- Update Customer Details (Allowed fields only)
            if (userInDb.getRoles().contains(UserRole.CUSTOMER)) {
                CustomerDetails updatedCustomerDetails = userInDb.getCustomerDetails();

                if (updatedCustomerDetails == null)
                    updatedCustomerDetails = new CustomerDetails();
                if (dto.getCustomerDetails() != null && dto.getCustomerDetails().getAddress() != null)
                    updatedCustomerDetails.setAddress(dto.getCustomerDetails().getAddress());

                userInDb.setCustomerDetails(updatedCustomerDetails);
            }

            if (userInDb.getRoles().contains(UserRole.DELIVERY_STAFF)) {
                StaffDetails updatedStaffDetails = userInDb.getStaffDetails();
                if (updatedStaffDetails == null) {
                    updatedStaffDetails = new StaffDetails();
                }

                if (dto.getStaffDetails() != null) {
                    // something in future
                }
            }

        }


    }

}
