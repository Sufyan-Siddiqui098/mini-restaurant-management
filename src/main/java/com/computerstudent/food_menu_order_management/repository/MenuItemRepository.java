package com.computerstudent.food_menu_order_management.repository;

import com.computerstudent.food_menu_order_management.entity.MenuItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends MongoRepository<MenuItem, ObjectId> {
    Optional<List<MenuItem>> findByChef_Id(ObjectId chefId);
}
