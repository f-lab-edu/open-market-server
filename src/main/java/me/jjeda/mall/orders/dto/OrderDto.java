package me.jjeda.mall.orders.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDto {

    private DeliveryDto deliveryDto;

    private List<OrderItemDto> orderItemDtoList;

    public Order from() {
        List<OrderItem> tempOrderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            tempOrderItems.add(orderItemDto.from());
        }

        return Order.builder()
                .delivery(this.deliveryDto.from())
                .orderItems(tempOrderItems)
                .status(OrderStatus.ORDER)
                .orderAt(LocalDateTime.now())
                .build();
    }
}
