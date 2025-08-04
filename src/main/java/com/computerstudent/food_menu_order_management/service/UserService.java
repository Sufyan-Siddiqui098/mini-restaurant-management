package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.dto.PasswordUpdateByAdminDTO;
import com.computerstudent.food_menu_order_management.dto.PasswordUpdateDTO;
import com.computerstudent.food_menu_order_management.dto.UserResponseDTO;
import com.computerstudent.food_menu_order_management.dto.UserUpdateDTO;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.exception.InvalidCredentialsException;
import com.computerstudent.food_menu_order_management.exception.PasswordMismatchException;
import com.computerstudent.food_menu_order_management.exception.UserNotFoundException;
import com.computerstudent.food_menu_order_management.mapper.UserMapper;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // ========= For Admin ======
    public List<UserResponseDTO> getAllUser() {
        User currentUser = authService.getCurrentUser();
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);
        if (!isAdmin) {
            throw new AccessDeniedException("Unauthorized to fetch all users.");
        }
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles(),
                user.getPhone(),
                user.getChefDetails(),
                user.getCustomerDetails(),
                user.getStaffDetails()
        )).collect(Collectors.toList());
    }

    public String updateUserPasswordByAdmin(String id, PasswordUpdateByAdminDTO dto) {
        ObjectId objectId = new ObjectId(id);
        User currentUser = authService.getCurrentUser();

        User userInDB = userRepository.findById(objectId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));

        // ---- Authorization check
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);

        if (!isAdmin) {
            throw new AccessDeniedException("Unauthorized User");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordMismatchException("New password and confirm password do not match");
        }

        userInDB.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userInDB);
        return "Password of " + userInDB.getUserName() + " Updated Successfully ";
    }

    // ============== General User ============
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
    public String deleteUserById(String id) {
        ObjectId objectId = new ObjectId(id);
        User currentUser = authService.getCurrentUser();

        User user = userRepository.findById(objectId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));

        // ---- Authorization check
        boolean isSelf = currentUser.getId().equals(user.getId());
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);

        if (!isSelf && !isAdmin) {
            throw new AccessDeniedException("Unauthorized User");
        }
        userRepository.deleteById(objectId);
        return "User {" + user.getUserName() + "} is deleted.";
    }

    // -- Update User (not password)
    public UserResponseDTO updateUserById(String id, UserUpdateDTO updateUser) {
        ObjectId objectId = new ObjectId(id);
        User currentUser = authService.getCurrentUser();

        User userInDB = userRepository.findById(objectId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));

        // ---- Authorization check
        boolean isSelf = currentUser.getId().equals(userInDB.getId());
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);
        if (!isSelf && !isAdmin) {
            throw new AccessDeniedException("Unauthorized User");
        }

        // updated the userInDB
        userMapper.updateUserFromDTO(updateUser, currentUser, userInDB);
        User savedUser = userRepository.save(userInDB);
        return userMapper.toUserResponseDTO(savedUser);
    }

    // -- Update Password - User
    public String updateUserPassword(String id, PasswordUpdateDTO dto) {
        ObjectId objectId = new ObjectId(id);
        User currentUser = authService.getCurrentUser();

        User userInDB = userRepository.findById(objectId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));

        // ---- Authorization check
        boolean isSelf = currentUser.getId().equals(userInDB.getId());

        if (!isSelf) {
            throw new AccessDeniedException("Unauthorized User");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), userInDB.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordMismatchException("New password and confirm password do not match");
        }

        userInDB.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(userInDB);
        return "Password of " + userInDB.getUserName() + " Updated Successfully ";
    }

}
