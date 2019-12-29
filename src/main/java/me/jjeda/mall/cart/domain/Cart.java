package me.jjeda.mall.cart.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RedisHash("cart")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Cart {

    @Id
    private String id;

    private List<CartItem> cartItemList;

    private Cart(String id) {
        this.id = id;
        this.cartItemList = new ArrayList<>();
    }

    public static Cart of(String id) {
        return new Cart(id);
    }
}
