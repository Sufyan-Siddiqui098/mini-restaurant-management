package com.computerstudent.food_menu_order_management.utils;

import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {
    public boolean isChefOrAdmin(User currentUser, User targetUser){
        return currentUser.getId().equals(targetUser.getId()) && currentUser.getRoles().contains(UserRole.CHEF)
                ||  currentUser.getRoles().contains(UserRole.ADMIN);
    }
}
