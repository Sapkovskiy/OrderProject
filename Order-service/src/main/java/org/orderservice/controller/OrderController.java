package org.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.orderservice.dto.OrderDTO;
import org.orderservice.model.Order;
import org.orderservice.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
@Tag(name = "Заказы", description = "Операции с заказами")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Создание заказа")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        String order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(String.format("Заказ №%s создан", order));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение заказа по ID")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    @Operation(summary = "Получение всех заказов")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/filter")
    @Operation(summary = "Фильтрация заказов по дате и минимальной сумме")
    public ResponseEntity<List<Order>> getFilteredOrders(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate date,
            @RequestParam BigDecimal minTotal) {
        return ResponseEntity.ok(orderService.getOrdersByDateAndTotal(date, minTotal));
    }

    @GetMapping("/filter-without-product")
    @Operation(summary = "Получение заказов без указанного продукта в указанном периоде")
    public ResponseEntity<List<Order>> getOrdersWithoutProductAndInPeriod(
            @RequestParam String productName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(orderService.getOrdersWithoutProductAndInPeriod(productName, startDate, endDate));
    }
}
