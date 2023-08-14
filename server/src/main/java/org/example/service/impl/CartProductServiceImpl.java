package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.Cart;
import org.example.model.CartProduct;
import org.example.model.CartProductKey;
import org.example.model.Product;
import org.example.repository.CartProductRepository;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.example.service.CartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.example.constants.ErrorMessage.*;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Set<CartProduct> getProductsFromCartByIdCart(Long id) {
        // id cart is valid
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(CART_NOT_FOUND, HttpStatus.NOT_FOUND));

        // cart does have product, doesn't ??
        if (!cartProductRepository.findByCart(cart).isEmpty()) {
            return cartProductRepository.findByCart(cart);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public CartProduct save(CartProduct cartProduct) {
        // set id for cart-product
        CartProductKey key = new CartProductKey();
        key.setCartId(cartProduct.getCart().getId());
        key.setProductId(cartProduct.getProduct().getId());
        cartProduct.setId(key);

        // cart-product is exists ? quantity++ : create new cart-product
        if (cartProductRepository.findById(cartProduct.getId()).isPresent()) {
            CartProduct cartProductDB = cartProductRepository.getById(cartProduct.getId());
            Integer quantityDB = cartProductDB.getQuantity();
            Product product = productRepository.getById(cartProduct.getProduct().getId());
            // check quantity of product in product table
            Integer quantityProduct = product.getQuantity();
            if (quantityProduct == 0) {
                throw new ApiRequestException(QUANTITY_IS_NOT_VALID, HttpStatus.BAD_REQUEST);
            }
            cartProductDB.setQuantity(quantityDB + cartProduct.getQuantity());
            product.setQuantity(quantityProduct - cartProduct.getQuantity());
            productRepository.save(product);
            return cartProductRepository.save(cartProductDB);
        } else {
            Cart cart = cartRepository.findById(cartProduct.getCart().getId())
                    .orElseThrow(() -> new ApiRequestException(CART_NOT_FOUND, HttpStatus.NOT_FOUND));
            Product product = productRepository.findById(cartProduct.getProduct().getId())
                    .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
            Integer quantityProduct = product.getQuantity();
            product.setQuantity(quantityProduct - cartProduct.getQuantity());
            productRepository.save(product);
            return cartProductRepository.save(cartProduct);
        }
    }

    @Override
    public void delete(CartProduct cartProduct) {
        // cart is valid
        Cart cart = cartRepository.findById(cartProduct.getCart().getId())
                .orElseThrow(() -> new ApiRequestException(CART_NOT_FOUND, HttpStatus.NOT_FOUND));
        // product is valid
        Product product = productRepository.findById(cartProduct.getProduct().getId())
                .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        // get quantity of product in product table
        Integer quantityProduct = product.getQuantity();

        // find cart-product
        CartProductKey key = new CartProductKey();
        key.setCartId(cart.getId());
        key.setProductId(product.getId());
        cartProduct.setId(key);
        CartProduct cartProductDB = cartProductRepository.findById(cartProduct.getId())
                .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND_IN_CART, HttpStatus.NOT_FOUND));
        // get quantity of product in cart-product table
        Integer quantityDB = cartProductDB.getQuantity();

        // quantity==0 (delete all) ? delete : quantityDB=quantityDB-1
        if (cartProduct.getQuantity() == 0) {
            cartProductRepository.deleteById(cartProductDB.getId());
            quantityProduct = quantityProduct + quantityDB;
            product.setQuantity(quantityProduct);
            productRepository.save(product);
        } else {
            quantityDB = quantityDB - 1;
            cartProductDB.setQuantity(quantityDB);
            cartProductRepository.save(cartProductDB);
            quantityProduct = quantityProduct + 1;
            product.setQuantity(quantityProduct);
            productRepository.save(product);
        }
    }
}
