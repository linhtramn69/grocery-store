package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.repository.OrderProductRepository;
import org.example.repository.OrdersRepository;
import org.example.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.example.constants.ErrorMessage.ORDER_NOT_FOUND;


@Service
public class OrderProductServiceImpl implements OrderProductService {
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Set<OrderProduct> findByIdOrder(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        Set<OrderProduct> orderProduct = orderProductRepository.findByOrder(orders);
        return orderProduct;
//        if (orderProduct.isEmpty()) {
//            throw new ApiRequestException(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
//        } else return orderProduct;
    }
}
