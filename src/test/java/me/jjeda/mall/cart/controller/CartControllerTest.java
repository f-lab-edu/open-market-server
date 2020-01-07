package me.jjeda.mall.cart.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.common.BaseControllerTest;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import me.jjeda.mall.cart.domain.Cart;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.repository.CartRedisRepository;
import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.items.domain.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CartControllerTest extends BaseControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartRedisRepository cartRedisRepository;

    @Autowired
    private AccountRepository accountRepository;

    Item item1 = Item.builder()
            .id(1L)
            .name("아이템1")
            .price(10000)
            .stockQuantity(100)
            .build();
    Item item2 = Item.builder()
            .id(2L)
            .name("아이템2")
            .price(20000)
            .stockQuantity(200)
            .build();

    CartItem cartItem1 = CartItem.builder()
            .item(item1)
            .price(item1.getPrice() * 2)
            .quantity(2)
            .build();
    CartItem cartItem2 = CartItem.builder()
            .item(item2)
            .price(item2.getPrice() * 3)
            .quantity(3)
            .build();

    @Before
    public void generateAccount() {
        AccountDto accountDto = AccountDto.builder()
                .accountRole(Set.of(AccountRole.USER))
                .address(new Address("a", "b", "c"))
                .email("jjeda@naver.com")
                .nickname("jjeda")
                .phone("01012341234")
                .password("pass")
                .build();
        accountService.saveAccount(accountDto);
    }

    @After
    public void setUp() {
        cartRedisRepository.deleteAll();
        accountRepository.deleteAll();
    }

    private String getAccessToken() throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("temp", "pass"))
                .param("username", "jjeda@naver.com")
                .param("password", "pass")
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return "bearer " + parser.parseMap(responseBody).get("access_token").toString();
    }

    @Test
    @TestDescription("정상적으로 장바구니를 만드는 테스트")
    public void createCart() throws Exception {

        mockMvc.perform(post("/api/carts")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("cartItemList").exists());
    }

    @Test
    @TestDescription("정상적으로 장바구니를 불러오는는 테스트")
    public void getCart() throws Exception {

        //given
        Cart cart = Cart.builder()
                .id("jjeda@naver.com")
                .cartItemList(List.of(cartItem1, cartItem2))
                .build();
        cartRedisRepository.save(cart);

        //when & then
        mockMvc.perform(get("/api/carts")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("cartItemList").exists())
                .andExpect(jsonPath("cartItemList[0].item").exists());
    }

    @Test
    @TestDescription("정상적으로 장바구니에 상품을 추가하는 테스트")
    public void add_item() throws Exception {

        //given
        Cart cart = Cart.builder()
                .id("jjeda@naver.com")
                .cartItemList(List.of(cartItem1))
                .build();
        cartRedisRepository.save(cart);

        //when & then
        mockMvc.perform(put("/api/carts/items/new")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(cartItem2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("cartItemList").exists())
                .andExpect(jsonPath("cartItemList[1].price").value(cartItem2.getPrice()))
                .andExpect(jsonPath("cartItemList[1].quantity").value(cartItem2.getQuantity()));
    }

    @Test
    @TestDescription("정상적으로 장바구니에 상품을 제거하는 테스트")
    public void remove_item() throws Exception {

        //given
        Cart cart = Cart.builder()
                .id("jjeda@naver.com")
                .cartItemList(List.of(cartItem1, cartItem2))
                .build();
        cartRedisRepository.save(cart);

        //when & then
        mockMvc.perform(put("/api/carts/remove")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(cartItem2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("cartItemList[0]").exists())
                .andExpect(jsonPath("cartItemList[1]").doesNotExist());
    }

    @Test(expected = NestedServletException.class)
    @TestDescription("정상적으로 장바구니 삭제하는 테스트")
    public void delete_cart() throws Exception {

        //given
        Cart cart = Cart.builder()
                .id("jjeda@naver.com")
                .cartItemList(List.of(cartItem1, cartItem2))
                .build();
        cartRedisRepository.save(cart);

        //when & then
        mockMvc.perform(delete("/api/carts")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/carts")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()));
    }
}