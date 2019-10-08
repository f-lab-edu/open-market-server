package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Builder
public class OrderDto {

    private DeliveryDto deliveryDto;

    private List<OrderItemDto> orderItemDtoList;

    private Account account;

    public Order from() {
        List<OrderItem> tempOrderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            tempOrderItems.add(orderItemDto.from());
        }

        return Order.builder()
                .delivery(this.deliveryDto.from())
                .orderItems(tempOrderItems)
                .account(this.account)
                .status(OrderStatus.ORDER)
                .orderAt(LocalDateTime.now())
                .build();
    }
}
