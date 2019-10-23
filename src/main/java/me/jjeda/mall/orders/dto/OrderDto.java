package me.jjeda.mall.orders.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDto {

    private DeliveryDto deliveryDto;

    private List<OrderItemDto> orderItemDtoList;

    public Order toEntity() {
        List<OrderItem> tempOrderItems = new ArrayList<>();
        orderItemDtoList.forEach((dto)->tempOrderItems.add(dto.toEntity()));

        return Order.builder()
                .delivery(this.deliveryDto.toEntity())
                .orderItems(tempOrderItems)
                .status(OrderStatus.ORDER)
                .orderAt(LocalDateTime.now())
                .build();
    }
}
