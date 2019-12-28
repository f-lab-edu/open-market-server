package me.jjeda.mall.cart.domain;

import lombok.Getter;
import me.jjeda.mall.items.domain.Item;

@Getter
public class CartItem {
    private Item item;
    private int price;
    private int quantity;
}
