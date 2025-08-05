package com.computerstudent.food_menu_order_management.exception;

public class MenuNotFoundException extends RuntimeException{
    public MenuNotFoundException(String message) {
        super(message);
    }
}
