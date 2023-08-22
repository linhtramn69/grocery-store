package org.example.service;

import org.example.model.OrderProduct;

import java.util.Set;

public interface OrderProductService {
    Set<OrderProduct> findByIdOrder(Long id);
}
