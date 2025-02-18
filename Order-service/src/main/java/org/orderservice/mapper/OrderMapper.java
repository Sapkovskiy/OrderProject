package org.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.orderservice.dto.OrderDTO;
import org.orderservice.dto.OrderItemDTO;
import org.orderservice.model.Order;
import org.orderservice.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "orderNumber", target = "orderNumber"),
            @Mapping(source = "totalAmount", target = "totalAmount"),
            @Mapping(source = "orderDate", target = "orderDate"),
            @Mapping(source = "customerName", target = "customerName"),
            @Mapping(source = "deliveryAddress", target = "deliveryAddress"),
            @Mapping(source = "paymentType", target = "paymentType"),
            @Mapping(source = "deliveryType", target = "deliveryType"),
            @Mapping(target = "items", ignore = true)
    })
    Order toOrder(OrderDTO orderDTO);

    @Named("orderItems")
    default List<OrderItem> orderItems(List<OrderItemDTO> items) {
        return items.stream()
                .map(OrderItemMapper.INSTANCE::toOrderItem)
                .collect(Collectors.toList());
    }
}
