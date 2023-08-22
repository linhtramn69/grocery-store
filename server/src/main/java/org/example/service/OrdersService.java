package org.example.service;

import org.example.model.Orders;
import org.example.response.TotalResponse;

import java.util.List;

public interface OrdersService {

    Orders checkout(Orders orders);

    Orders update(Long id, Integer status);

    Orders findOrderById(Long id);

    List<Orders> findAllOrders();

    List<TotalResponse> getTotalByYear(int year);
}
