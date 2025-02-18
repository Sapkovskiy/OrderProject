package org.orderservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.orderservice.dto.OrderDTO;
import org.orderservice.model.Order;
import org.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderDTO orderDTO = new OrderDTO();
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn("12345");

        ResponseEntity<String> response = orderController.createOrder(orderDTO);

        verify(orderService, times(1)).createOrder(orderDTO);
        assertEquals(ResponseEntity.ok("Заказ №12345 создан"), response);
    }

    @Test
    void testGetOrder() {
        Order order = new Order();
        when(orderService.getOrderById(1)).thenReturn(order);

        ResponseEntity<Order> response = orderController.getOrder(1);

        verify(orderService, times(1)).getOrderById(1);
        assertEquals(ResponseEntity.ok(order), response);
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        verify(orderService, times(1)).getAllOrders();
        assertEquals(ResponseEntity.ok(orders), response);
    }

    @Test
    void testGetFilteredOrders() {
        LocalDate date = LocalDate.now();
        BigDecimal minTotal = BigDecimal.valueOf(100);
        Order order1 = new Order();
        List<Order> orders = Arrays.asList(order1);

        when(orderService.getOrdersByDateAndTotal(date, minTotal)).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getFilteredOrders(date, minTotal);

        verify(orderService, times(1)).getOrdersByDateAndTotal(date, minTotal);
        assertEquals(ResponseEntity.ok(orders), response);
    }

    @Test
    void testGetOrdersWithoutProductAndInPeriod() {
        String productName = "Product A";
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        Order order1 = new Order();
        List<Order> orders = Arrays.asList(order1);

        when(orderService.getOrdersWithoutProductAndInPeriod(productName, startDate, endDate)).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getOrdersWithoutProductAndInPeriod(productName, startDate, endDate);

        verify(orderService, times(1)).getOrdersWithoutProductAndInPeriod(productName, startDate, endDate);
        assertEquals(ResponseEntity.ok(orders), response);
    }
}
