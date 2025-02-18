package org.orderservice.service;


import lombok.extern.slf4j.Slf4j;
import org.orderservice.dto.OrderDTO;
import org.orderservice.exception.OrderCreationException;
import org.orderservice.mapper.OrderItemMapper;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.model.Order;
import org.orderservice.model.OrderItem;
import org.orderservice.repository.OrderItemRepository;
import org.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderItemRepository orderItemRepository;

    @Value("${number.service.url}")
    private String numberServiceUrl;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public String createOrder(OrderDTO orderDTO) {
        String orderNumber = restTemplate.postForObject(numberServiceUrl + "/numbers", null, String.class);

        if (orderNumber == null || orderNumber.isEmpty()) {
            throw new OrderCreationException("Ошибка при генерации номера заказа", null);
        }

        orderDTO.setOrderNumber(orderNumber);
        Order order = OrderMapper.INSTANCE.toOrder(orderDTO);
        order.setOrderDate(LocalDate.now());

        Order savedOrder = orderRepository.save(order);

        // Гарантируем, что список не будет null
        List<OrderItem> orderItems = Optional.ofNullable(orderDTO.getItems())
                .orElse(Collections.emptyList()) // Если null, подставляем пустой список
                .stream()
                .map(itemDTO -> {
                    OrderItem item = OrderItemMapper.INSTANCE.toOrderItem(itemDTO);
                    item.setOrderId(savedOrder.getId()); // Привязываем товар к заказу
                    return item;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        return orderNumber;
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).stream()
                .peek(order -> order.setItems(orderRepository.findOrderItemsByOrderId(order.getId())))
                .findFirst()
                .orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrders().stream()
                .peek(order -> order.setItems(orderRepository.findOrderItemsByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByDateAndTotal(LocalDate date, BigDecimal minTotal) {
        List<Order> orders = orderRepository.findByDateAndTotal(date, minTotal);
        orders.forEach(order -> order.setItems(orderRepository.findOrderItemsByOrderId(order.getId())));
        return orders;
    }

    public List<Order> getOrdersWithoutProductAndInPeriod(String productName, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findOrdersWithoutProductAndInPeriod(productName, startDate, endDate);
        orders.forEach(order -> order.setItems(orderRepository.findOrderItemsByOrderId(order.getId())));
        return orders;
    }
}