package com.szymonszopinski.ordermngmntsystem.controller;

import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl theOrderService) {
        this.orderService = theOrderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Order> addOrder( @Valid @RequestBody Order order) {
        Order newOrder = orderService.addOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order, @PathVariable Long id) {
        Order updatedOrder = orderService.updateOrder(order, id);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public  void  deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);

    }

    @GetMapping("/order/{id}")
    public Order getOrderById(@PathVariable Long id) {
       return orderService.getOrderById(id);
    }
}

