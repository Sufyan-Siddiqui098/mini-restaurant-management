package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.LoginResponseDTO;
import com.computerstudent.food_menu_order_management.dto.CustomerSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.UserLoginDTO;
import com.computerstudent.food_menu_order_management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private AuthService authService; // login and sign up


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO user) {
        LoginResponseDTO loginResponseDTO = authService.login(user);
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> customerSignup(@Valid @RequestBody CustomerSignUpDTO customer){
        String successMessage = authService.customerSignUp(customer);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);

    }

}
