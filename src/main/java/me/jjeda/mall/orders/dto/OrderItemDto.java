package me.jjeda.mall.orders.dto;

import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.orders.domain.OrderItem;

public class OrderItemDto {

    private int orderPrice;

    private int quantity;

    private ItemDto itemDto;

    public OrderItem from() {
        return OrderItem.builder()
                .orderPrice(this.orderPrice)
                .quantity(this.quantity)
                .item(this.itemDto.from())
                .build();
    }
}
