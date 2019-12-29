package me.jjeda.mall.cart.service;

import me.jjeda.mall.cart.domain.Cart;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.repository.CartRedisRepository;
import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.items.domain.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;
    @Mock
    private CartRedisRepository cartRedisRepository;

    @Test
    @TestDescription("정상적으로 장바구니 생성하는 테스트")
    public void initCart_success() {
        //when
        Cart cart = cartService.initCart("jjeda");

        //then
        assertThat(cart.getId()).isEqualTo("jjeda");
        assertThat(cart.getCartItemList().size()).isEqualTo(0);
    }

    @Test
    @TestDescription("장바구니가 이미 만들어져 있을 때 불러오는 테스트")
    public void initCart_existed_cart() {
        //given
        Cart newCart = Cart.of("jjeda");
        List<CartItem> list = newCart.getCartItemList();
        CartItem cartItem = CartItem.builder().build();
        list.add(cartItem);

        given(cartRedisRepository.existsById("jjeda")).willReturn(Boolean.TRUE);
        given(cartRedisRepository.findById("jjeda")).willReturn(Optional.of(newCart));

        //when
        Cart cart = cartService.initCart("jjeda");

        //then
        assertThat(cart.getId()).isEqualTo("jjeda");
        assertThat(cart.getCartItemList().get(0)).isEqualTo(cartItem);
    }

    @Test
    @TestDescription("장바구니에 상품을 추가하는 테스트")
    public void add_item() {
        Cart newCart = Cart.of("jjeda");
        CartItem cartItem = CartItem.builder().build();
        given(cartRedisRepository.findById("jjeda")).willReturn(Optional.of(newCart));

        //when
        cartService.addItem("jjeda", cartItem);

        //then
        assertThat(newCart.getCartItemList().get(0)).isEqualTo(cartItem);

    }

    @Test
    @TestDescription("장바구니에 상품을 제거하는 테스트")
    public void remove_item_success() {
        //given
        Cart newCart = Cart.of("jjeda");
        List<CartItem> list = newCart.getCartItemList();
        Item item = Item.builder()
                .id(1L)
                .stockQuantity(100)
                .price(5000)
                .name("아이템")
                .build();
        CartItem cartItem = CartItem.builder()
                .item(item)
                .price(item.getPrice() * 2)
                .quantity(2)
                .build();
        list.add(cartItem);
        given(cartRedisRepository.findById("jjeda")).willReturn(Optional.of(newCart));

        // 상품, 개수, 가격이 모두 일치해야 같은 상품으로 취급
        CartItem newCartItem = CartItem.builder()
                .item(item)
                .quantity(2)
                .price(10000)
                .build();

        //when
        cartService.removeItem("jjeda", newCartItem);

        //then
        assertThat(newCart.getCartItemList().size()).isEqualTo(0);
    }

    @Test
    @TestDescription("같은 상품이라도 장바구니 정보와 일치하지 않으면 제거되지 않는 테스트1")
    public void remove_item_fail1() {
        //given
        Cart newCart = Cart.of("jjeda");
        List<CartItem> list = newCart.getCartItemList();
        Item item = Item.builder()
                .id(1L)
                .stockQuantity(100)
                .price(5000)
                .name("아이템")
                .build();
        CartItem cartItem = CartItem.builder()
                .item(item)
                .price(item.getPrice() * 2)
                .quantity(2)
                .build();
        list.add(cartItem);
        given(cartRedisRepository.findById("jjeda")).willReturn(Optional.of(newCart));

        // 상품, 개수, 가격이 모두 일치해야 같은 상품으로 취급
        CartItem newCartItem = CartItem.builder()
                .item(item)
                .quantity(1) //수량이 다르면
                .price(10000)
                .build();

        //when
        cartService.removeItem("jjeda", newCartItem);

        //then
        assertThat(newCart.getCartItemList().size()).isNotEqualTo(0);
    }

    @Test
    @TestDescription("같은 상품이라도 장바구니 정보와 일치하지 않으면 제거되지 않는 테스트2")
    public void remove_item_fail2() {
        //given
        Cart newCart = Cart.of("jjeda");
        List<CartItem> list = newCart.getCartItemList();
        Item item = Item.builder()
                .id(1L)
                .stockQuantity(100)
                .price(5000)
                .name("아이템")
                .build();
        CartItem cartItem = CartItem.builder()
                .item(item)
                .price(item.getPrice() * 2)
                .quantity(2)
                .build();
        list.add(cartItem);
        given(cartRedisRepository.findById("jjeda")).willReturn(Optional.of(newCart));

        // 상품, 개수, 가격이 모두 일치해야 같은 상품으로 취급
        CartItem newCartItem = CartItem.builder()
                .item(item)
                .quantity(2)
                .price(20000) //가격이 다르면
                .build();

        //when
        cartService.removeItem("jjeda", newCartItem);

        //then
        assertThat(newCart.getCartItemList().size()).isNotEqualTo(0);
    }
}