package me.jjeda.mall.cart.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.jjeda.mall.items.domain.Item;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CartItem {
    private Item item;
    private int price;
    private int quantity;
}
