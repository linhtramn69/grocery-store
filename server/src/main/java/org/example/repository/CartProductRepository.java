package org.example.repository;

import org.example.model.Cart;
import org.example.model.CartProduct;
import org.example.model.CartProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductKey> {
    Set<CartProduct> findByCart(Cart cart);
}
