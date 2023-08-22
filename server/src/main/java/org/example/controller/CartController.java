package org.example.controller;

import org.example.model.Cart;
import org.example.model.CartProduct;
import org.example.model.Product;
import org.example.service.CartProductService;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping("/getByUser/{id}")
    public ResponseEntity<?> findByIdUser(@PathVariable("id") Long id) {
        Cart cart = cartService.findByIdUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
