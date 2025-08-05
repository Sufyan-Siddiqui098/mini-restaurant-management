package com.computerstudent.food_menu_order_management.service;


import com.computerstudent.food_menu_order_management.dto.CreateMenuItemDTO;
import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.entity.MenuItem;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.exception.MenuNotFoundException;
import com.computerstudent.food_menu_order_management.exception.UserNotFoundException;
import com.computerstudent.food_menu_order_management.mapper.MenuMapper;
import com.computerstudent.food_menu_order_management.repository.MenuItemRepository;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private MenuMapper menuMapper;

    // ---- Create menu
    public MenuItemResponseDTO createMenuItemForChef(String chefId, CreateMenuItemDTO dto) {
        User currentUser = authService.getCurrentUser();
        User chef = userRepository.findById(new ObjectId(chefId))
                .orElseThrow(() -> new UserNotFoundException("User not find with ID " + chefId));

        // ---- Authorization check
        boolean isSelf = currentUser.getId().equals(chef.getId()) && currentUser.getRoles().contains(UserRole.CHEF);
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);
        // ----- If not the same user or Admin
        if (!isSelf && !isAdmin) {
            throw new AccessDeniedException("Unauthorized User");
        }

        // mapping
        MenuItem menu = menuMapper.toMenuItemFromCreateMenu(dto, chef);
        MenuItem savedMenu = menuItemRepository.save(menu);
        // mapping
        return menuMapper.toMenuItemResponseDTO(savedMenu);
    }

    // --- GET All menus
    public List<MenuItemResponseDTO> getAllMenusOfChef(String chefId) {
        User currentUser = authService.getCurrentUser();
        User chef = userRepository.findById(new ObjectId(chefId))
                .orElseThrow(() -> new UserNotFoundException("User not find with ID " + chefId));

        // ---- Authorization check
        boolean isSelf = currentUser.getId().equals(chef.getId()) && currentUser.getRoles().contains(UserRole.CHEF);
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);
        // ----- If not the same user or Admin
        if (!isSelf && !isAdmin) {
            throw new AccessDeniedException("Unauthorized User");
        }

        List<MenuItem> menuByChef = menuItemRepository.findByChef_Id(new ObjectId(chefId))
                .orElseThrow(() -> new MenuNotFoundException("No menu under chef id " + chefId));

        return menuByChef
                .stream()
                .map(menuItem -> menuMapper.toMenuItemResponseDTO(menuItem))
                .toList();
    }
}
