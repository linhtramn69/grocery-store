package org.example.service;

import org.example.model.CartProduct;

import java.util.Set;

public interface CartProductService {
    CartProduct save(CartProduct cartProduct);

    void delete(CartProduct cartProduct);

    Set<CartProduct> getProductsFromCartByIdCart(Long id);
}
