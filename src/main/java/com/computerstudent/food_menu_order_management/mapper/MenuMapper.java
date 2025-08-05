package com.computerstudent.food_menu_order_management.mapper;

import com.computerstudent.food_menu_order_management.dto.ChefResponseDTO;
import com.computerstudent.food_menu_order_management.dto.CreateMenuItemDTO;
import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.entity.MenuItem;
import com.computerstudent.food_menu_order_management.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    @Autowired
    private UserMapper userMapper;

    public MenuItemResponseDTO toMenuItemResponseDTO(MenuItem savedMenu){
        MenuItemResponseDTO response = new MenuItemResponseDTO();
        response.setId(savedMenu.getId());
        response.setName(savedMenu.getName());
        response.setDescription(savedMenu.getDescription());
        response.setPrice(savedMenu.getPrice());
        response.setTags(savedMenu.getTags());
        response.setAvailable(savedMenu.isAvailable());
        // Map user to Chef Response DTO
        ChefResponseDTO chefResponseDTO = userMapper.toChefResponseDTO(savedMenu.getChef());
        response.setChef(chefResponseDTO);

        return response;
    }

    public MenuItem toMenuItemFromCreateMenu(CreateMenuItemDTO dto, User chef){
        MenuItem menu = new MenuItem();
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());
        menu.setPrice(dto.getPrice());
        menu.setTags(dto.getTags());
        menu.setAvailable(dto.isAvailable());
        menu.setChef(chef);

        return menu;
    }
}
