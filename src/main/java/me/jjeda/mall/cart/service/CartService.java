package me.jjeda.mall.cart.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.cart.domain.Cart;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.repository.CartRedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRedisRepository cartRedisRepository;

    public Cart getCart(String id) {
        return cartRedisRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Cart addItem(String id, CartItem cartItem) {
        final Cart cart;

        if (!cartRedisRepository.existsById(id)) {
            cart = Cart.of(id);
        } else {
            cart = getCart(id);
        }

        List<CartItem> cartItemList = cart.getCartItemList();
        cartItemList.add(cartItem);
        return cartRedisRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String id, CartItem cartItem) {
        Cart cart = getCart(id);
        List<CartItem> cartItemList = cart.getCartItemList();
        cartItemList.remove(cartItem);

        return cartRedisRepository.save(cart);
    }

    @Transactional
    public void deleteCart(String id) {
        cartRedisRepository.delete(getCart(id));
    }
}
