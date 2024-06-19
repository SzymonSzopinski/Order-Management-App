package com.szymonszopinski.ordermngmntsystem.service;

import com.szymonszopinski.ordermngmntsystem.entity.Order;

import java.util.List;

public interface IOrderService {
    Order addOrder(Order order);


    List<Order> getOrders();

    Order updateOrder(Order order, Long id);

    Order getOrderById(Long id);

    void deleteOrder(Long id);




}
