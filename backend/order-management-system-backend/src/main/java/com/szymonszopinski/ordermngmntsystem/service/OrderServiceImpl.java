package com.szymonszopinski.ordermngmntsystem.service;

import com.szymonszopinski.ordermngmntsystem.entity.Customer;
import com.szymonszopinski.ordermngmntsystem.entity.OrderedItem;
import com.szymonszopinski.ordermngmntsystem.exception.OrderNotFoundException;
import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.repository.OrderRepository;
import com.szymonszopinski.ordermngmntsystem.repository.OrderedItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderedItemsRepository orderedItemsRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderedItemsRepository orderedItemsRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemsRepository = orderedItemsRepository;
    }

    @Override
    @Transactional
    public Order addOrder(Order order) {
        order.getOrderItem().forEach(order::add);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateOrder(Order orderDetails, Long id) {
        return orderRepository.findById(id).map(existingOrder -> {
            updateOrderDetails(existingOrder, orderDetails);
            updateCustomer(existingOrder.getCustomer(), orderDetails.getCustomer());
            updateOrderedItems(existingOrder, orderDetails.getOrderItem());
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }

    private void updateOrderDetails(Order existingOrder, Order orderDetails) {
        existingOrder.setStatus(orderDetails.getStatus());
        existingOrder.setNoteToOrder(orderDetails.getNoteToOrder());
        existingOrder.setBranch(orderDetails.getBranch());
        existingOrder.setDateCreated(orderDetails.getDateCreated());
        existingOrder.setTotalPrice(orderDetails.getTotalPrice());
        existingOrder.setDeliveryMethod(orderDetails.getDeliveryMethod());
        existingOrder.setPaymentMethod(orderDetails.getPaymentMethod());
    }

    private void updateOrderedItems(Order existingOrder, Set<OrderedItem> updatedItems) {
        Set<Long> updatedItemIds = updatedItems.stream()
                .map(OrderedItem::getId)
                .collect(Collectors.toSet());

        Set<OrderedItem> itemsToRemove = existingOrder.getOrderItem().stream()
                .filter(item -> !updatedItemIds.contains(item.getId()))
                .collect(Collectors.toSet());

        itemsToRemove.forEach(item -> {
            existingOrder.getOrderItem().remove(item);
            orderedItemsRepository.delete(item);
        });

        updatedItems.forEach(updatedItem -> {
            existingOrder.getOrderItem().stream()
                    .filter(item -> item.getId() != null && item.getId().equals(updatedItem.getId()))
                    .findFirst()
                    .ifPresentOrElse(existingItem -> {
                        existingItem.setQuantity(updatedItem.getQuantity());
                        existingItem.setUnitPrice(updatedItem.getUnitPrice());
                        existingItem.setProductId(updatedItem.getProductId());
                    }, () -> {
                        updatedItem.setOrder(existingOrder);
                        existingOrder.getOrderItem().add(updatedItem);
                    });
        });
    }

    private void updateCustomer(Customer existingCustomer, Customer updatedCustomer) {
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        existingCustomer.getAddress().setCity(updatedCustomer.getAddress().getCity());
        existingCustomer.getAddress().setCountry(updatedCustomer.getAddress().getCountry());
        existingCustomer.getAddress().setProvince(updatedCustomer.getAddress().getProvince());
        existingCustomer.getAddress().setStreet(updatedCustomer.getAddress().getStreet());
        existingCustomer.getAddress().setHouseNumber(updatedCustomer.getAddress().getHouseNumber());
        existingCustomer.getAddress().setZipCode(updatedCustomer.getAddress().getZipCode());

        existingCustomer.setCustomerNoteToOrder(updatedCustomer.getCustomerNoteToOrder());
    }


    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }
}
