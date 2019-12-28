package me.jjeda.mall.cart.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.repository.CartRedisRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRedisRepository cartRedisRepository;

    public Object initCart(Long id) {
    }

    public Object getCart(Long id) {
    }

    public Object addItem(Long id, CartItem cartItem) {
    }

    public Object removeItem(Long id, CartItem cartItem) {
    }

    public Object deleteCart(Long id) {
    }
}
