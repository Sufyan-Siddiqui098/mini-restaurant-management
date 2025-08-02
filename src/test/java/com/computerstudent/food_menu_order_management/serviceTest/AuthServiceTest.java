package com.computerstudent.food_menu_order_management.serviceTest;

import com.computerstudent.food_menu_order_management.dto.ChefSignUpDTO;
import com.computerstudent.food_menu_order_management.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void customerSignupTest(){
        ChefSignUpDTO dto = new ChefSignUpDTO();
        dto.setEmail("chef@gmail.com");
        dto.setUserName("chef");
        dto.setFirstName("David");
        dto.setLastName("Ben");
        dto.setPhone("2348230423");
        dto.setPassword("12345678");
        dto.setSpecialization(Arrays.asList("Chinese", "Desi"));
        String chefSignUp = authService.chefSignUp(dto);
        Assertions.assertNotNull(chefSignUp);
    }
}
