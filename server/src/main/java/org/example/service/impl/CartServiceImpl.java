package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.Cart;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.example.constants.ErrorMessage.CART_NOT_FOUND;
import static org.example.constants.ErrorMessage.USER_NOT_FOUND;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Cart findByIdUser(Long id) {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return cartRepository.findByUser(userDB)
                .orElseThrow(() -> new ApiRequestException(CART_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
