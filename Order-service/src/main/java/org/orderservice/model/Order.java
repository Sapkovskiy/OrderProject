package org.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private int id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String customerName;
    private String deliveryAddress;
    private String paymentType;
    private String deliveryType;

    @Transient
    private List<OrderItem> items;
}
