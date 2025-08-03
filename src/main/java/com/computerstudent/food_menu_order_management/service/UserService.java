package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.exception.UserNotFoundException;
import com.computerstudent.food_menu_order_management.mapper.UserMapper;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserMapper userMapper;




    // --- GET User
    public UserResponseDTO getUserById(String id) {
        ObjectId objectId = new ObjectId(id);

        User currentUser = authService.getCurrentUser();
        User user = userRepository.findById(objectId).orElseThrow(() -> new UserNotFoundException("User not find with ID " + id));

        // ----- If not the same user or Admin
        if (!currentUser.getId().equals(user.getId()) && !currentUser.getRoles().contains(UserRole.ADMIN)) {
            throw new AccessDeniedException("Unauthorized User");
        }

        // --- Mapping for response
        return userMapper.toUserResponseDTO(user);

    }

    // --- Delete User
    public void deleteUserById(String id) {

    }

}
