package com.computerstudent.food_menu_order_management.repository;

import com.computerstudent.food_menu_order_management.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid Email format") String email);
}
