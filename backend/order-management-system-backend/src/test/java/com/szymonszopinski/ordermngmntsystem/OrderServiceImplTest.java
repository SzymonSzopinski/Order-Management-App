package com.szymonszopinski.ordermngmntsystem;

import com.szymonszopinski.ordermngmntsystem.entity.Address;
import com.szymonszopinski.ordermngmntsystem.entity.Customer;
import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.entity.OrderedItem;
import com.szymonszopinski.ordermngmntsystem.exception.OrderNotFoundException;
import com.szymonszopinski.ordermngmntsystem.repository.OrderRepository;
import com.szymonszopinski.ordermngmntsystem.repository.OrderedItemsRepository;
import com.szymonszopinski.ordermngmntsystem.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final String BRANCH_WROCLAW = "Wroclaw";
    private static final String BRANCH_KATOWICE = "Katowice";
    private static final String STATUS_NEW = "New";
    private static final String STATUS_SHIPPED = "Shipped";
    private static final String DELIVERY_METHOD_UPS = "UPS";
    private static final String DELIVERY_METHOD_DHL = "DHL";
    private static final String PAYMENT_METHOD_CREDIT_CARD = "Credit Card";
    private static final BigDecimal TOTAL_PRICE_100 = BigDecimal.valueOf(100);
    private static final BigDecimal TOTAL_PRICE_200 = BigDecimal.valueOf(200);

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderedItemsRepository orderedItemsRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order1 = createOrder(ORDER_ID_1, BRANCH_WROCLAW, STATUS_NEW, DELIVERY_METHOD_UPS, PAYMENT_METHOD_CREDIT_CARD, TOTAL_PRICE_100, "john.doe@example.com");
        order2 = createOrder(ORDER_ID_2, BRANCH_KATOWICE, STATUS_SHIPPED, DELIVERY_METHOD_DHL, PAYMENT_METHOD_CREDIT_CARD, TOTAL_PRICE_200, "jane.smith@example.com");
    }

    private Order createOrder(Long id, String branch, String status, String deliveryMethod, String paymentMethod, BigDecimal totalPrice, String email) {
        Customer customer = new Customer();
        customer.setFirstName("FirstName" + id);
        customer.setLastName("LastName" + id);
        customer.setEmail(email);

        Address address = new Address();
        address.setCity("City" + id);
        address.setCountry("Country" + id);
        address.setProvince("Province" + id);
        address.setStreet("Street" + id);
        address.setHouseNumber("HouseNumber" + id);
        address.setZipCode("ZipCode" + id);
        customer.setAddress(address);

        OrderedItem item = new OrderedItem();
        item.setProductId(id);
        item.setQuantity(id.intValue());
        item.setUnitPrice(BigDecimal.valueOf(10.00 * id));

        Set<OrderedItem> orderedItems = new HashSet<>();
        orderedItems.add(item);

        Order order = new Order();
        order.setId(id);
        order.setBranch(branch);
        order.setStatus(status);
        order.setDateCreated(LocalDate.now());
        order.setDeliveryMethod(deliveryMethod);
        order.setPaymentMethod(paymentMethod);
        order.setCustomer(customer);
        order.setOrderItem(orderedItems);
        order.setTotalPrice(totalPrice);
        return order;
    }

    @Test
    void testAddOrder() {
        when(orderRepository.save(order1)).thenReturn(order1);

        Order savedOrder = orderService.addOrder(order1);

        assertEquals(order1, savedOrder, "Order should be added correctly");
        assertEquals("FirstName1", savedOrder.getCustomer().getFirstName(), "Customer first name should match");
        assertEquals(1, savedOrder.getOrderItem().size(), "Order items size should match");
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testGetOrders() {
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getOrders();

        assertEquals(2, result.size(), "Number of orders should match");
        assertEquals(order1, result.get(0), "First order should match");
        assertEquals(order2, result.get(1), "Second order should match");
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrder() {
        Order updatedOrder = createUpdatedOrder();

        when(orderRepository.findById(ORDER_ID_1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrder(updatedOrder, ORDER_ID_1);

        assertUpdatedOrder(result, updatedOrder);
    }

    private Order createUpdatedOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setId(ORDER_ID_1);
        updatedOrder.setStatus(STATUS_SHIPPED);
        updatedOrder.setNoteToOrder("Updated note");
        updatedOrder.setBranch("Updated branch");
        updatedOrder.setDateCreated(LocalDate.now().plusDays(1));
        updatedOrder.setTotalPrice(BigDecimal.valueOf(200.00));
        updatedOrder.setDeliveryMethod("Updated delivery");
        updatedOrder.setPaymentMethod("Updated payment");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setLastName("Doe");
        updatedCustomer.setEmail("jane.doe@example.com");
        updatedCustomer.setPhoneNumber("987654321");

        Address updatedAddress = new Address();
        updatedAddress.setCity("Warsaw");
        updatedAddress.setCountry("Poland");
        updatedAddress.setProvince("Mazovia");
        updatedAddress.setStreet("Second Street");
        updatedAddress.setHouseNumber("456");
        updatedAddress.setZipCode("00-456");
        updatedCustomer.setAddress(updatedAddress);

        updatedOrder.setCustomer(updatedCustomer);

        OrderedItem updatedItem = new OrderedItem();
        updatedItem.setId(ORDER_ID_1);
        updatedItem.setQuantity(4);
        updatedItem.setUnitPrice(BigDecimal.valueOf(50.00));
        updatedItem.setProductId(ORDER_ID_1);
        updatedOrder.getOrderItem().add(updatedItem);
        updatedItem.setOrder(updatedOrder);

        return updatedOrder;
    }

    private void assertUpdatedOrder(Order result, Order updatedOrder) {
        assertEquals(STATUS_SHIPPED, result.getStatus(), "Order status should be updated");
        assertEquals("Updated note", result.getNoteToOrder(), "Note to order should be updated");
        assertEquals("Updated branch", result.getBranch(), "Branch should be updated");
        assertEquals(updatedOrder.getDateCreated(), result.getDateCreated(), "Date created should be updated");
        assertEquals(updatedOrder.getTotalPrice(), result.getTotalPrice(), "Total price should be updated");
        assertEquals("Updated delivery", result.getDeliveryMethod(), "Delivery method should be updated");
        assertEquals("Updated payment", result.getPaymentMethod(), "Payment method should be updated");

        assertEquals("Jane", result.getCustomer().getFirstName(), "Customer first name should be updated");
        assertEquals("Doe", result.getCustomer().getLastName(), "Customer last name should be updated");
        assertEquals("jane.doe@example.com", result.getCustomer().getEmail(), "Customer email should be updated");
        assertEquals("987654321", result.getCustomer().getPhoneNumber(), "Customer phone number should be updated");

        assertEquals("Warsaw", result.getCustomer().getAddress().getCity(), "Customer city should be updated");
        assertEquals("Poland", result.getCustomer().getAddress().getCountry(), "Customer country should be updated");
        assertEquals("Mazovia", result.getCustomer().getAddress().getProvince(), "Customer province should be updated");
        assertEquals("Second Street", result.getCustomer().getAddress().getStreet(), "Customer street should be updated");
        assertEquals("456", result.getCustomer().getAddress().getHouseNumber(), "Customer house number should be updated");
        assertEquals("00-456", result.getCustomer().getAddress().getZipCode(), "Customer zip code should be updated");
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(ORDER_ID_1)).thenReturn(Optional.of(order1));

        Order result = orderService.getOrderById(ORDER_ID_1);

        assertEquals(order1, result, "Retrieved order should match the expected order");
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(ORDER_ID_1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(ORDER_ID_1), "Exception should be thrown if order not found");
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(ORDER_ID_1)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(ORDER_ID_1);

        orderService.deleteOrder(ORDER_ID_1);

        verify(orderRepository, times(1)).deleteById(ORDER_ID_1);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(ORDER_ID_1)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(ORDER_ID_1), "Exception should be thrown if order not found");
    }

}
