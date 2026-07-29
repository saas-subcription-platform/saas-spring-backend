package com.saas.springbackend.user.controller;

import com.saas.springbackend.user.dto.UpdateProfileDTO;
import com.saas.springbackend.user.dto.UserRequestDTO;
import com.saas.springbackend.user.dto.UserResponseDTO;
import com.saas.springbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Add User
    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.addUser(requestDTO));
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User By Id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update User
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileDTO dto) {

        return ResponseEntity.ok(userService.updateProfile(id, dto));
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByCompany(@PathVariable Long companyId) {
        List<UserResponseDTO> users = userService.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }
}