package me.jjeda.mall.cart.repository;

import me.jjeda.mall.cart.domain.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRedisRepository extends CrudRepository<Cart, String> {
}
