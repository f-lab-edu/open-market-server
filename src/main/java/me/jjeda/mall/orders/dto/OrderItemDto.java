package me.jjeda.mall.orders.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.orders.domain.OrderItem;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemDto {

    private int orderPrice;

    private int quantity;

    private ItemDto itemDto;

    public OrderItem toEntity() {
        return OrderItem.builder()
                .orderPrice(this.orderPrice)
                .quantity(this.quantity)
                .item(this.itemDto.toEntity())
                .build();
    }
}

