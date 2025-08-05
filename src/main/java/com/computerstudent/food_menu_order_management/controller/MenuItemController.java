package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.CreateMenuItemDTO;
import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chef/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/{chefId}")
    public ResponseEntity<?> createMenuItemOfChef(@PathVariable String chefId, @RequestBody CreateMenuItemDTO createMenu) {
        MenuItemResponseDTO menuItemForChef = menuItemService.createMenuItemForChef(chefId, createMenu);
        return new ResponseEntity<>(menuItemForChef, HttpStatus.OK);
    }

    @GetMapping("/{chefId}")
    public ResponseEntity<?> getAllMenuItemsOfChef(@PathVariable String chefId) {
        List<MenuItemResponseDTO> allMenusOfChef = menuItemService.getAllMenusOfChef(chefId);
        return new ResponseEntity<>(allMenusOfChef, HttpStatus.OK);
    }
}
