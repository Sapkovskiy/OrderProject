package org.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Данные о заказе")
public class OrderDTO {
    @NotBlank
    private String orderNumber;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Schema(description = "Общая сумма заказа", example = "100.00")
    private BigDecimal totalAmount;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime orderDate;
    @NotBlank
    @Schema(description = "Имя заказчика", example = "Иван Петров")
    private String customerName;
    @NotBlank
    @Schema(description = "Адрес доставки", example = "г. Москва, ул. Пушкина, д. 1")
    private String deliveryAddress;
    @NotBlank
    @Schema(description = "Тип оплаты", example = "Онлайн")
    private String paymentType;
    @NotBlank
    @Schema(description = "Тип доставки", example = "Самовывоз")
    private String deliveryType;
    @NotEmpty
    private List<OrderItemDTO> items;
}
