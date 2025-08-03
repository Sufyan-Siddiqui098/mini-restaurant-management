package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.config.model.CustomUserDetails;
import com.computerstudent.food_menu_order_management.config.service.UserDetailsServiceImpl;
import com.computerstudent.food_menu_order_management.dto.ChefSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.LoginResponseDTO;
import com.computerstudent.food_menu_order_management.dto.CustomerSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.UserLoginDTO;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.exception.DuplicateResourceException;
import com.computerstudent.food_menu_order_management.exception.UserNotFoundException;
import com.computerstudent.food_menu_order_management.mapper.UserMapper;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import com.computerstudent.food_menu_order_management.config.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;



    public String customerSignUp(CustomerSignUpDTO customer) {

        if (userRepository.existsByEmail(customer.getEmail()))
            throw new DuplicateResourceException("Email already in use");
        if (userRepository.existsByUserName(customer.getUserName()))
            throw new DuplicateResourceException("Username already exist");

        User user = userMapper.fromCustomerSignupDTOtoUser(customer);
        User save = userRepository.save(user);
        return "Sign up successfully ";

    }

    public String chefSignUp(ChefSignUpDTO chef) {

        if (userRepository.existsByEmail(chef.getEmail()))
            throw new DuplicateResourceException("Email already in exist");
        if (userRepository.existsByUserName(chef.getUserName()))
            throw new DuplicateResourceException("Username already exist");

        User user = userMapper.fromChefSignupDTOtoUser(chef);

        User save = userRepository.save(user);

        return "Sign up successfully ";

    }

    public LoginResponseDTO login(UserLoginDTO credentials) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String token = jwtUtil.generateToken(authentication);

            return userMapper.fromUserToLoginResponseDTO(user, token);

        } catch (AuthenticationException e) {
            log.error("Error while login ", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", e);
        }
    }

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if(user == null){
           throw new UserNotFoundException("Current user not found");
        }
        return user;

    }
}
