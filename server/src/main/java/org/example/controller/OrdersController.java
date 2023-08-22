package org.example.controller;

import org.example.model.Orders;
import org.example.response.TotalResponse;
import org.example.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllOrders() {
        List<Orders> list = ordersService.findAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Orders orders = ordersService.findOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/getTotalByYear/{year}")
    public ResponseEntity<?> getTotalByYear(@PathVariable("year") int year) {
        List<TotalResponse> list = ordersService.getTotalByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Orders order) {
        Orders newOrder = ordersService.checkout(order);
        return ResponseEntity.status(HttpStatus.OK).body(newOrder);
    }

    @PatchMapping("/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody Orders order) {
        Orders newOrder = ordersService.update(id, order.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(newOrder);
    }

}
