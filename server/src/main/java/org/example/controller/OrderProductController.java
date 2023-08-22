package org.example.controller;

import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/order-details")
public class OrderProductController {
    @Autowired
    private OrderProductService orderProductService;

    @GetMapping("/getOrderDetails/{id}")
    public ResponseEntity<?> getOrderDetailsByIdOrder(@PathVariable Long id){
        Set<OrderProduct> orderProduct = orderProductService.findByIdOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderProduct);
    }

}
