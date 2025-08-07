package com.computerstudent.food_menu_order_management.service;


import com.computerstudent.food_menu_order_management.dto.MenuItemDTO;
import com.computerstudent.food_menu_order_management.dto.MenuItemResponseDTO;
import com.computerstudent.food_menu_order_management.entity.MenuItem;
import com.computerstudent.food_menu_order_management.entity.User;
import com.computerstudent.food_menu_order_management.enums.UserRole;
import com.computerstudent.food_menu_order_management.exception.MenuNotFoundException;
import com.computerstudent.food_menu_order_management.exception.UserNotFoundException;
import com.computerstudent.food_menu_order_management.mapper.MenuMapper;
import com.computerstudent.food_menu_order_management.repository.MenuItemRepository;
import com.computerstudent.food_menu_order_management.repository.UserRepository;
import com.computerstudent.food_menu_order_management.utils.AuthorizationUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private AuthorizationUtil authorizationUtil;

    // ---- Create menu
    public MenuItemResponseDTO createMenuItemForChef(String chefId, MenuItemDTO dto) {
        User chef = getAuthorizedChef(chefId);
        // mapping
        MenuItem menu = menuMapper.toMenuItemFromMenuDTO(dto, chef);
        MenuItem savedMenu = menuItemRepository.save(menu);
        log.info("Menu {} created for chef-id {}", savedMenu.getName(), chefId);
        // mapping
        return menuMapper.toMenuItemResponseDTO(savedMenu);
    }

    // --- GET All menus
    public List<MenuItemResponseDTO> getAllMenusForChef(String chefId) {
        User chef = getAuthorizedChef(chefId);
        List<MenuItem> menuByChef = menuItemRepository.findByChef_Id(chef.getId())
                .orElseThrow(() -> new MenuNotFoundException("No menu under chef id " + chefId));

        log.info("All menus of chef-id {} are fetched ", chefId);
        return menuByChef
                .stream()
                .map(menuItem -> menuMapper.toMenuItemResponseDTO(menuItem))
                .toList();
    }

    // ---- Get Menu By ID
    public MenuItemResponseDTO getMenuItemByIdForChef(String chefId, String menuId) {
        // check if authorized
        getAuthorizedChef(chefId);
        MenuItem menuItem = menuItemRepository.findById(new ObjectId(menuId))
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id " + menuId));
        // Ownership validation
        if(!menuItem.getChef().getId().equals(new ObjectId(chefId)))
            throw new MenuNotFoundException("You don't own this menu");

        log.info("Menu item with id {} fetched by chef-id {}", menuId, chefId);
        return menuMapper.toMenuItemResponseDTO(menuItem);
    }

    // ---- Update Menu
    public MenuItemResponseDTO updateMenuItemForChef(String chefId, String menuId, MenuItemDTO dto) {
        // check if authorized
        getAuthorizedChef(chefId);
        MenuItem menuInDb = menuItemRepository.findById(new ObjectId(menuId))
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id " + menuId));

        if(!menuInDb.getChef().getId().equals(new ObjectId(chefId)))
            throw new AccessDeniedException("You don't have permission to delete this menu-item.");

        // Update the menu in DB
        menuMapper.updateMenuItemFromDTO(menuInDb, dto);
        MenuItem savedMenu = menuItemRepository.save(menuInDb);
        log.info("Menu {} is updated", savedMenu.getName());
        return menuMapper.toMenuItemResponseDTO(savedMenu);
    }

    // ---- Delete Menu By ID
    public void deleteMenuItemById(String chefId, String menuId) {
        ObjectId menuObjectId = new ObjectId(menuId);
        // check if authorized
        getAuthorizedChef(chefId);
        MenuItem menuInDb = menuItemRepository.findById(menuObjectId)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id " + menuId));

        if(!menuInDb.getChef().getId().equals(new ObjectId(chefId)))
            throw new AccessDeniedException("You don't have permission to delete this menu-item.");

        menuItemRepository.deleteById(menuObjectId);
        log.info("Menu is deleted for menu-id: {} ", menuId);
    }


    // ================================ GLOBAL MENU ACCESS ================================

    // Get All Active menu-item
    public List<MenuItemResponseDTO> getAllActiveMenuItemGlobally() {
        List<MenuItem> allMenuItems = menuItemRepository.findAll();

        if (allMenuItems.isEmpty())
            throw new MenuNotFoundException("No Menu is present at the moment");

        log.info("All Active menus fetched");
        return allMenuItems.stream().filter(MenuItem::isAvailable)
                .map(menuItem -> menuMapper.toMenuItemResponseDTO(menuItem))
                .toList();
    }

    // Get Chef specific Menu item
    public List<MenuItemResponseDTO> getActiveMenuItemOfChefGlobally(String chefId) {
        List<MenuItem> menuByChef = menuItemRepository.findByChef_Id(new ObjectId(chefId))
                .orElseThrow(() -> new MenuNotFoundException("No menu under chef id " + chefId));

        return menuByChef.stream().filter(MenuItem::isAvailable)
                .map(menuItem -> menuMapper.toMenuItemResponseDTO(menuItem))
                .toList();
    }

    // ---- Get Menu By ID
    public MenuItemResponseDTO getMenuItemByIdGlobally(String menuId) {
        MenuItem menuItem = menuItemRepository.findById(new ObjectId(menuId))
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id " + menuId));
        if(!menuItem.isAvailable())
            throw new MenuNotFoundException("Menu is not active");

        return menuMapper.toMenuItemResponseDTO(menuItem);
    }

    // --- Delete Menu when corresponding chef is deleted
    public void deleteAndReturnMenusByChef(ObjectId chefId) {
        List<MenuItem> toDelete = menuItemRepository.findByChef_Id(chefId)
                .orElse(Collections.emptyList());
        if (!toDelete.isEmpty()) {
            menuItemRepository.deleteAll(toDelete);
            log.info("Deleted {} menu items for chef {}", toDelete.size(), chefId.toHexString());
        }
        log.info("No menu-item were present for chef-id : {}", chefId.toHexString());
    }


    // ------------ HELPER --------
    private User getAuthorizedChef(String chefId) {
        User currentUser = authService.getCurrentUser();
        User chef = userRepository.findById(new ObjectId(chefId))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + chefId));
        if (!authorizationUtil.isChefOrAdmin(currentUser, chef)) {
            throw new AccessDeniedException("Unauthorized User");
        }
        return chef;
    }

}
