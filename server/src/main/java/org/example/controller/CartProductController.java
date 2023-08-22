package org.example.controller;

import org.example.model.CartProduct;
import org.example.service.CartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/cart-product")
public class CartProductController {

    @Autowired
    private CartProductService cartProductService;

    @GetMapping("/get-products-from-cart/{id}")
    public ResponseEntity<?> getProductsFromCartByIdCart(@PathVariable Long id){
        Set<CartProduct> data = cartProductService.getProductsFromCartByIdCart(id);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(@RequestBody CartProduct cartProduct) {
        CartProduct newData = cartProductService.save(cartProduct);
        return ResponseEntity.status(HttpStatus.OK).body(newData);
    }

    @DeleteMapping("/delete-product-cart")
    public ResponseEntity deleteProductFromCart(@RequestBody CartProduct cartProduct) {
        cartProductService.delete(cartProduct);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
