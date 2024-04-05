package com.szymonszopinski.ordermngmntsystem.service;

import com.szymonszopinski.ordermngmntsystem.entity.Customer;
import com.szymonszopinski.ordermngmntsystem.entity.OrderedItems;
import com.szymonszopinski.ordermngmntsystem.exception.OrderNotFoundException;
import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.repository.OrderRepository;
import com.szymonszopinski.ordermngmntsystem.repository.OrderedItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService{

    private OrderRepository orderRepository;
    private OrderedItemsRepository orderedItemsRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository, OrderedItemsRepository theOrderedItemsRepository) {
        this.orderRepository = theOrderRepository;
        this.orderedItemsRepository = theOrderedItemsRepository;
    }

    @Override
    @Transactional
    public Order addOrder(Order order) {

        Set<OrderedItems> orderItems = order.getOrderItem();
        orderItems.forEach(item -> order.add(item));
//        order.getOrderItem().forEach(order::add);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }


    @Transactional
    @Override
    public Order updateOrder(Order orderDetails, Long id) {
        return orderRepository.findById(id).map(existingOrder -> {
            existingOrder.setStatus(orderDetails.getStatus());
            existingOrder.setNoteToOrder(orderDetails.getNoteToOrder());
            existingOrder.setBranch(orderDetails.getBranch());
            existingOrder.setDateCreated(orderDetails.getDateCreated());
            existingOrder.setTotalPrice(orderDetails.getTotalPrice());
            existingOrder.setDeliveryMethod(orderDetails.getDeliveryMethod());
            existingOrder.setPaymentMethod(orderDetails.getPaymentMethod());

            // Aktualizacja danych klienta
            Customer existingCustomer = existingOrder.getCustomer();
            Customer updatedCustomer = orderDetails.getCustomer();
            updateCustomer(existingCustomer, updatedCustomer);

            updateOrderedItems(existingOrder, orderDetails.getOrderItem());
            
            // Zapisz zmiany w zamówieniu
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }

    private void updateOrderedItems(Order existingOrder, Set<OrderedItems> updatedItems) {
        // Usuń przedmioty, które nie są obecne w aktualizowanym zamówieniu
        Set<Long> updatedItemIds = updatedItems.stream()
                .map(OrderedItems::getId)
                .collect(Collectors.toSet());

        Set<OrderedItems> itemsToRemove = existingOrder.getOrderItem().stream()
                .filter(item -> !updatedItemIds.contains(item.getId()))
                .collect(Collectors.toSet());

        // Najpierw usuń przedmioty, które mają być usunięte
        itemsToRemove.forEach(item -> {
            existingOrder.getOrderItem().remove(item);
            orderedItemsRepository.delete(item);
        });

        // Aktualizuj istniejące przedmioty i dodaj nowe
        updatedItems.forEach(updatedItem -> {
            // Sprawdź, czy przedmiot już istnieje w zamówieniu
            Optional<OrderedItems> existingItemOpt = existingOrder.getOrderItem().stream()
                    .filter(item -> item.getId() != null && item.getId().equals(updatedItem.getId()))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                // Jeśli istnieje, zaktualizuj istniejący przedmiot
                OrderedItems existingItem = existingItemOpt.get();
                existingItem.setQuantity(updatedItem.getQuantity());
                existingItem.setUnitPrice(updatedItem.getUnitPrice());
                existingItem.setProductId(updatedItem.getProductId());
                // Możesz tutaj dodać więcej pól do aktualizacji
            } else {
                // Jeśli to nowy przedmiot, ustaw relację i dodaj do kolekcji
                updatedItem.setOrder(existingOrder);
                existingOrder.getOrderItem().add(updatedItem);
            }
        });
    }




    private void updateCustomer(Customer tempCustomer, Customer updatedCustomer) {

        tempCustomer.setFirstName(updatedCustomer.getFirstName());
        tempCustomer.setLastName(updatedCustomer.getLastName());
        tempCustomer.setEmail(updatedCustomer.getEmail());
        tempCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        tempCustomer.getAddress().setCity(updatedCustomer.getAddress().getCity());
        tempCustomer.getAddress().setCountry(updatedCustomer.getAddress().getCountry());
        tempCustomer.getAddress().setProvince(updatedCustomer.getAddress().getProvince());
        tempCustomer.getAddress().setStreet(updatedCustomer.getAddress().getStreet());
        tempCustomer.getAddress().setZipCode(updatedCustomer.getAddress().getZipCode());
    }


    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Sorry, order not found"));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
