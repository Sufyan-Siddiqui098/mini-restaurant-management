package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/menu-items")
public class GlobalMenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<?> getAllActiveMenus(){
        List<MenuItemResponseDTO> allActiveMenuItemGlobally = menuItemService.getAllActiveMenuItemGlobally();
        return new ResponseEntity<>(allActiveMenuItemGlobally, HttpStatus.OK);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<?> getActiveMenuById(@PathVariable String menuId){
        MenuItemResponseDTO menuItem = menuItemService.getMenuItemByIdGlobally(menuId);
        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<?> getAllActiveMenuItemOfChef(@PathVariable String chefId){
        List<MenuItemResponseDTO> menuOfChefList = menuItemService.getActiveMenuItemOfChefGlobally(chefId);
        return new ResponseEntity<>(menuOfChefList, HttpStatus.OK);
    }
}
