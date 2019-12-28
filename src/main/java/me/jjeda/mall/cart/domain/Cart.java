package me.jjeda.mall.cart.domain;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

public class Cart {

    @Id
    private String id;

    private List<CartItem> cartItemList;

    public Cart(String id) {
        this.id = id;
        this.cartItemList = new LinkedList<>();
    }
}
