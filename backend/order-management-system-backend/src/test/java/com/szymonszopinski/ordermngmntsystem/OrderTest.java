package com.szymonszopinski.ordermngmntsystem;

import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.entity.OrderedItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testAddOrderedItemToOrder() {
        Order order = new Order();
        OrderedItem item = new OrderedItem();

        order.add(item);

        assertTrue(order.getOrderItem().contains(item), "Order should contain the added item");
        assertEquals(order, item.getOrder(), "Item's order reference should be set correctly");
    }

    @Test
    void testAddMultipleOrderedItemsToOrder() {
        Order order = new Order();
        OrderedItem item1 = new OrderedItem();
        OrderedItem item2 = new OrderedItem();

        order.add(item1);
        order.add(item2);

        assertTrue(order.getOrderItem().contains(item1), "Order should contain the first added item");
        assertTrue(order.getOrderItem().contains(item2), "Order should contain the second added item");
        assertEquals(order, item1.getOrder(), "First item's order reference should be set correctly");
        assertEquals(order, item2.getOrder(), "Second item's order reference should be set correctly");
    }
}

