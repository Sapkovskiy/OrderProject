package org.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.orderservice.dto.OrderDTO;
import org.orderservice.exception.OrderCreationException;
import org.orderservice.model.Order;
import org.orderservice.repository.OrderItemRepository;
import org.orderservice.repository.OrderRepository;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber("ORD123");
        orderDTO.setItems(Collections.emptyList());

        when(restTemplate.postForObject(anyString(), isNull(), eq(String.class)))
                .thenReturn("ORD123");
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        String result = orderService.createOrder(orderDTO);

        assertEquals("ORD123", result);
    }

    @Test
    void testCreateOrder_Fail_NumberServiceDown() {
        OrderDTO orderDTO = new OrderDTO();

        when(restTemplate.postForObject(anyString(), isNull(), eq(String.class)))
                .thenReturn(null);

        assertThrows(OrderCreationException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void testGetOrderById_Found() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNumber("ORD123");

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals("ORD123", result.getOrderNumber());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        Order result = orderService.getOrderById(1);

        assertNull(result);
    }

    @Test
    void testGetAllOrders() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNumber("ORD123");

        when(orderRepository.findAllOrders()).thenReturn(List.of(order));

        List<Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void testGetOrdersByDateAndTotal() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNumber("ORD123");

        when(orderRepository.findByDateAndTotal(any(), any()))
                .thenReturn(List.of(order));

        List<Order> result = orderService.getOrdersByDateAndTotal(LocalDate.now(), new BigDecimal("50.00"));

        assertEquals(1, result.size());
    }

    @Test
    void testGetOrdersWithoutProductAndInPeriod() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNumber("ORD123");

        when(orderRepository.findOrdersWithoutProductAndInPeriod(any(), any(), any()))
                .thenReturn(List.of(order));

        List<Order> result = orderService.getOrdersWithoutProductAndInPeriod("Product1", LocalDate.now().minusDays(10), LocalDate.now());

        assertEquals(1, result.size());
    }
}
