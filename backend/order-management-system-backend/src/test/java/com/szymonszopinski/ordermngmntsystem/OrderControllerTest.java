package com.szymonszopinski.ordermngmntsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szymonszopinski.ordermngmntsystem.controller.OrderController;
import com.szymonszopinski.ordermngmntsystem.entity.Order;
import com.szymonszopinski.ordermngmntsystem.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order1 = new Order();
        order1.setId(1L);
        order1.setBranch("Wroclaw");
        order1.setStatus("New");
        order1.setDateCreated(LocalDate.now());
        order1.setDeliveryMethod("UPS");
        order1.setPaymentMethod("Credit Card");
        order1.setTotalPrice(BigDecimal.valueOf(100));

        order2 = new Order();
        order2.setId(2L);
        order2.setBranch("Katowice");
        order2.setStatus("Shipped");
        order2.setDateCreated(LocalDate.now());
        order2.setDeliveryMethod("DHL");
        order2.setPaymentMethod("Credit Card");
        order2.setTotalPrice(BigDecimal.valueOf(200));
    }

    @Test
    void testGetOrders() throws Exception {
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(order1.getId().intValue())))
                .andExpect(jsonPath("$[0].branch", is(order1.getBranch())))
                .andExpect(jsonPath("$[1].id", is(order2.getId().intValue())))
                .andExpect(jsonPath("$[1].branch", is(order2.getBranch())));
    }

    @Test
    void testAddOrder() throws Exception {
        when(orderService.addOrder(any(Order.class))).thenReturn(order1);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(order1.getId().intValue())))
                .andExpect(jsonPath("$.branch", is(order1.getBranch())));
    }

    @Test
    void testUpdateOrder() throws Exception {
        when(orderService.updateOrder(any(Order.class), eq(order1.getId()))).thenReturn(order1);

        mockMvc.perform(put("/orders/update/{id}", order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order1.getId().intValue())))
                .andExpect(jsonPath("$.branch", is(order1.getBranch())));
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(order1.getId());

        mockMvc.perform(delete("/orders/delete/{id}", order1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(order1.getId());
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(order1.getId())).thenReturn(order1);

        mockMvc.perform(get("/orders/order/{id}", order1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order1.getId().intValue())))
                .andExpect(jsonPath("$.branch", is(order1.getBranch())));
    }
}
