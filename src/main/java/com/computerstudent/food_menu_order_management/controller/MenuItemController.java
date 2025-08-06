package com.computerstudent.food_menu_order_management.controller;

import com.computerstudent.food_menu_order_management.dto.MenuItemDTO;
import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chefs/{chefId}/menu-items")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    // POST /chefs/{chefId}/menu-items
    @PostMapping
    public ResponseEntity<?> createMenuItemOfChef(@PathVariable String chefId, @RequestBody MenuItemDTO createMenu) {
        MenuItemResponseDTO menuItemForChef = menuItemService.createMenuItemForChef(chefId, createMenu);
        return new ResponseEntity<>(menuItemForChef, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllMenuItemsOfChef(@PathVariable String chefId) {
        List<MenuItemResponseDTO> allMenusOfChef = menuItemService.getAllMenusForChef(chefId);
        return new ResponseEntity<>(allMenusOfChef, HttpStatus.OK);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<?> getAllMenuItemOfChef(@PathVariable String chefId, @PathVariable String menuId){
        MenuItemResponseDTO menuItemByIdForChef = menuItemService.getMenuItemByIdForChef(chefId, menuId);
        return new ResponseEntity<>(menuItemByIdForChef, HttpStatus.OK);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenuItemOfChef(@PathVariable String chefId,
                                                  @PathVariable String menuId,
                                                  @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemResponseDTO updatedMenu = menuItemService.updateMenuItemForChef(chefId, menuId, menuItemDTO);
        return new ResponseEntity<>(updatedMenu, HttpStatus.OK);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenuItemOfChef(@PathVariable String chefId, @PathVariable String menuId){
        menuItemService.deleteMenuItemById(chefId, menuId);
        return new ResponseEntity<>("Menu Deleted Successfully", HttpStatus.OK);
    }
}
