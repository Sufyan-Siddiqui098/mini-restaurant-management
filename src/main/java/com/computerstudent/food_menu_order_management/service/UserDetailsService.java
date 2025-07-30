package com.computerstudent.food_menu_order_management.service;

import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getEmail()) // identifier for login - can be phone, email, username
                    .password(user.getPassword())
                    // stream to convert enum into string
                    .roles(user.getRoles().stream().map(UserRole::name).toArray(String[]::new))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }


}
