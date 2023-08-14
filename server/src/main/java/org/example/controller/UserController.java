package org.example.controller;

import org.example.model.Product;
import org.example.model.User;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> listUser = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable("username") String username) {
        User newUser = userService.update(user, username);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
