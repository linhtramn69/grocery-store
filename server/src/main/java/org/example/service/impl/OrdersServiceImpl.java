package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.*;
import org.example.repository.*;
import org.example.response.TotalResponse;
import org.example.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.example.constants.ErrorMessage.*;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    private Double total = 0.0;

    @Override
    public Orders findOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return orders;
    }

    @Override
    public List<Orders> findAllOrders() {
        List<Orders> list = ordersRepository.findAll();
        return list;
    }

    @Override
    public List<TotalResponse> getTotalByYear(int year) {
        List<TotalResponse> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            TotalResponse res = new TotalResponse();
            res.setMonth(i);
            Double total = ordersRepository.getTotalByMonthAndYear(i, year);
            if (total == null) {
                res.setTotal(0.0);
            } else {
                res.setTotal(total);
            }
            list.add(res);
        }
        return list;
    }

    @Override
    public Orders checkout(Orders orders) {
        // cart is valid
        Cart cartDB = cartRepository.findById(orders.getCart().getId())
                .orElseThrow(() -> new ApiRequestException(CART_NOT_FOUND, HttpStatus.NOT_FOUND));
        // cart has product, haven't ??
        Set<CartProduct> listProduct = cartProductRepository.findByCart(cartDB);
        if (listProduct.isEmpty()) {
            throw new ApiRequestException(PRODUCT_NOT_FOUND_IN_CART, HttpStatus.NOT_FOUND);
        }
        // if cart has product =>
        listProduct.stream().forEach(item -> {
            // product is valid
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
            total = total + (product.getPrice() * item.getQuantity());
        });
        // create and save new order ( total, status )
        orders.setTotal(total);
        orders.setStatus(0);
        Orders newOrder = ordersRepository.save(orders);

        // get and save data for order-product
        listProduct.stream().forEach(item -> {
            OrderProduct orderProduct = new OrderProduct();
            // set id and data for order-product
            OrderProductKey key = new OrderProductKey();
            key.setOrderId(newOrder.getId());
            key.setProductId(item.getProduct().getId());
            orderProduct.setId(key);
            orderProduct.setOrder(newOrder);
            orderProduct.setProduct(item.getProduct());
            orderProduct.setQuantity(item.getQuantity());
            orderProductRepository.save(orderProduct);
        });
        // remove product from cart => empty cart
        listProduct.stream().forEach(item -> {
            cartProductRepository.deleteById(item.getId());
        });
        return newOrder;
    }

    @Override
    public Orders update(Long id, Integer status) {
        Orders orderDB = ordersRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        orderDB.setStatus(status);
        return ordersRepository.save(orderDB);
    }
}
