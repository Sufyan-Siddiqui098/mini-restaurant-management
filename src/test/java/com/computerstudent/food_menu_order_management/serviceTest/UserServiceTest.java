package com.computerstudent.food_menu_order_management.serviceTest;

import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void userGetByIdTest(){
        UserResponseDTO userById = userService.getUserById("688a773a0572748ba2707c2c");
        Assertions.assertNotNull(userById);
    }
}
