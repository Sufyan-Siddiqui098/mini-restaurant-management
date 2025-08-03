package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UserResponseDTO userById = userService.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        String deletedMessage = userService.deleteUserById(id);
        return new ResponseEntity<>(deletedMessage, HttpStatus.OK);
    }

}
