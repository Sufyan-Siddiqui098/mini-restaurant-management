# 🍽️ Mini Restaurant - Spring Boot Backend API

A role-based backend system for a restaurant platform built with **Spring Boot**, implementing secure user authentication, RESTful APIs, and user-role-specific access control for managing menu items.

---

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **MongoDB**
- **Maven**

---

## 📌 Features

### 🔐 Authentication & Authorization
- JWT-based login and session management
- Three types of users: **Admin**, **Chef**, and **Customer**
- Role-based route protection using and filter chain

### 👤 Users
- **Signup & Login**:
  - `/public/signup/chef`
  - `/public/signup/customer`
  - `/public/login`
- **User CRUD** (Authenticated users):
  - `GET /user/{id}`
  - `PUT /user/{id}`
  - `DELETE /user/{id}`
  - `PUT /user/{id}/password`

### 🧑‍🍳 Chef-Specific Menu APIs
Routes like `/chefs/{chefId}/menu-items` are only accessible by:
- The chef who owns the menu
- Admins (with full control)

Endpoints:
- `POST` - Create a new menu item
- `GET` - List all menu items for a chef
- `GET /{menuId}` - Get menu by ID
- `PUT /{menuId}` - Update menu item
- `DELETE /{menuId}` - Delete menu item

### 🌍 Public Menu APIs
Accessible to all:
- `GET /public/menu-items` - All active menus
- `GET /public/menu-items/{menuId}` - Get menu by ID
- `GET /public/menu-items/chef/{chefId}` - Active menus by chef

### 🛡️ Admin Access
Admins can:
- Add or delete any user
- Manage any menu item

> Admin endpoints: `/admin/**` (You can detail these if you have a separate controller)

---

## 🗂️ Project Structure

```
src
├── config (SecurityConfig, JWT filter, etc.)
├── controller (MenuItemController, GlobalMenuItemController, UserController, PublicController)
├── dto (DTOs for MenuItem, User, Auth)
├── service (Service interfaces & implementations)
├── entity (User, MenuItem, etc.)
└── repository (Spring Data Repositories)
```

---

## 🔒 Security Configuration

- JWT tokens handled via `JwtFilter` and `UsernamePasswordAuthenticationFilter`
- Stateless sessions using `SessionCreationPolicy.STATELESS`
- Endpoints access defined via:
  ```java
  .requestMatchers("/chef/**").hasRole("CHEF")
  .requestMatchers("/admin/**").hasRole("ADMIN")
  .requestMatchers("/user/**").authenticated()
  .requestMatchers("/public/**").permitAll()
  ```

---

