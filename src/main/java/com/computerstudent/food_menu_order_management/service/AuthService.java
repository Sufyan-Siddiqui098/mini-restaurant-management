package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.config.model.CustomUserDetails;
import com.computerstudent.food_menu_order_management.config.service.UserDetailsServiceImpl;
import com.computerstudent.food_menu_order_management.dto.ChefSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.LoginResponseDTO;
import com.computerstudent.food_menu_order_management.dto.CustomerSignUpDTO;
import com.computerstudent.food_menu_order_management.dto.UserLoginDTO;
import com.computerstudent.food_menu_order_management.entity.ChefDetails;
import com.computerstudent.food_menu_order_management.entity.CustomerDetails;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.exception.DuplicateResourceException;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import com.computerstudent.food_menu_order_management.config.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public String customerSignUp(CustomerSignUpDTO customer) {

        if (userRepository.existsByEmail(customer.getEmail()))
            throw new DuplicateResourceException("Email already in use");
        if (userRepository.existsByUserName(customer.getUserName()))
            throw new DuplicateResourceException("Username already exist");

        User user = new User();
        user.setEmail(customer.getEmail());
        user.setUserName(customer.getUserName());
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

    }

    public String chefSignUp(ChefSignUpDTO chef) {

        if (userRepository.existsByEmail(chef.getEmail()))
            throw new DuplicateResourceException("Email already in use");
        if (userRepository.existsByUserName(chef.getUserName()))
            throw new DuplicateResourceException("Username already exist");

        User user = new User();
        user.setEmail(chef.getEmail());
        user.setUserName(chef.getUserName());
        user.setPassword(passwordEncoder.encode(chef.getPassword()));
        user.setPhone(chef.getPhone());
        user.setFirstName(chef.getFirstName());
        user.setLastName(chef.getLastName());
        user.setRoles(List.of(UserRole.CHEF));

        if (user.getChefDetails() == null) {
            user.setChefDetails(new ChefDetails());
        }

        user.getChefDetails().setSpecialization(chef.getSpecialization());

        User save = userRepository.save(user);

        return "Sign up successfully ";

    }

    public LoginResponseDTO login(UserLoginDTO credentials) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authentication);
            return new LoginResponseDTO(
                    userDetails.getUser().getId(),
                    userDetails.getUser().getEmail(),
                    userDetails.getUser().getUserName(),
                    userDetails.getUser().getFirstName(),
                    userDetails.getUser().getLastName(),
                    userDetails.getUser().getRoles(),
                    userDetails.getUser().getPhone(),
                    userDetails.getUser().getCustomerDetails().getAddress(),
                    token
            );

        } catch (AuthenticationException e) {
            log.error("Error while login ", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", e);
        }
    }
}
