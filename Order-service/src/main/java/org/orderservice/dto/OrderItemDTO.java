package org.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
@Schema(name = "Товары в заказе")
public class OrderItemDTO {
    @NotBlank
    @Schema(name = "productSku", example = "123456789")
    private String productSku;
    @NotBlank
    @Schema(name = "productName", example = "Продукт")
    private String productName;
    @NotNull
    @Min(1)
    @Schema(name = "quantity", example = "1")
    private int quantity;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Schema(name = "price", example = "100.00")
    private BigDecimal price;
}
