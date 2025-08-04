package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.PasswordUpdateDTO;
import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.dto.UserUpdateDTO;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable String id, @RequestBody UserUpdateDTO userUpdate){
        UserResponseDTO updatedUser = userService.updateUserById(id, userUpdate);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody PasswordUpdateDTO dto){
        String message = userService.updateUserPassword(id, dto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
