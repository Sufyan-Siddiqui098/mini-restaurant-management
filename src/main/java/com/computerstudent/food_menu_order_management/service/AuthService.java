package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.dto.CustomerSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.UserLoginDTO;
import com.computerstudent.food_menu_order_management.entity.CustomerDetails;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import com.computerstudent.food_menu_order_management.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public String customerSignUp(CustomerSignUpDTO customer) {
        try {
            if (userRepository.existsByEmail(customer.getEmail()))
                throw new RuntimeException("Email already in use");

            User user = new User();
            user.setEmail(customer.getEmail());
            user.setPassword(passwordEncoder.encode(customer.getPassword()));
            user.setPhone(customer.getPhone());
            user.setFirstName(customer.getFirstName());
            user.setLastName(customer.getLastName());
            user.setRoles(List.of(UserRole.CUSTOMER));
//            user.setAddress(customer.getAddress());
            if (user.getCustomerDetails() == null) {
                user.setCustomerDetails(new CustomerDetails());
            }
            user.getCustomerDetails().setAddress(customer.getAddress());
            User save = userRepository.save(user);
            return "Sign up successfully ";
        } catch (Exception e) {
            log.error("Error while sign up customer ", e);
            throw new RuntimeException("Something went wrong while sign up", e);
        }
    }

    public String login(UserLoginDTO credentials) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

            return jwtUtil.generateToken(authentication);

        } catch (AuthenticationException e) {
            log.error("Error while login ", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", e);
        }
    }
}
