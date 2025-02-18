package org.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.orderservice.dto.OrderItemDTO;
import org.orderservice.model.OrderItem;

@Mapper
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);
}
