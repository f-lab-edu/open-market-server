package me.jjeda.mall.cart.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.cart.domain.Cart;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.repository.CartRedisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRedisRepository cartRedisRepository;
    private final RedisTemplate redisTemplate;

    public Cart getCart(String id) {
        return cartRedisRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Cart addItem(String id, CartItem cartItem) {

        final String key = String.format("cart:%s", id);
        // "_class", "id" 필드를 제외하고 "cartItemList[n]" 의 필드개수 6개로 나누어주면 상품개수
        int cartItemSize = (int)((redisTemplate.opsForHash().size(key) - 2) / 6);
        boolean hasKey = redisTemplate.hasKey(key);
        final Map<String, Object> map;

        if (hasKey) {
            map = convertCartItemToMap(cartItemSize,cartItem);
        } else {
            map = convertCartItemToMap(0,cartItem);
        }

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch(key);
                redisOperations.multi();
                redisOperations.opsForHash().putAll(key,map);
                return redisOperations.exec();
            }
        });

        return getCart(id);
    }

    private Map<String, Object> convertCartItemToMap(int size, CartItem cartItem) {
        Map<String, Object> map = new HashMap<>();
        map.put(String.format("cartItemList.[%d].item.id", size), cartItem.getItem().getId());
        map.put(String.format("cartItemList.[%d].item.name", size), cartItem.getItem().getName());
        map.put(String.format("cartItemList.[%d].item.price", size), cartItem.getItem().getPrice());
        map.put(String.format("cartItemList.[%d].item.stockQuantity", size), cartItem.getItem().getStockQuantity());
        map.put(String.format("cartItemList.[%d].price", size), cartItem.getPrice());
        map.put(String.format("cartItemList.[%d].quantity", size), cartItem.getQuantity());
        return map;
    }

    public Cart removeItem(String id, CartItem cartItem) {
        Cart cart = getCart(id);
        List<CartItem> cartItemList = cart.getCartItemList();
        cartItemList.remove(cartItem);

        return cartRedisRepository.save(cart);
    }

    public void deleteCart(String id) {
        cartRedisRepository.delete(getCart(id));
    }
}
