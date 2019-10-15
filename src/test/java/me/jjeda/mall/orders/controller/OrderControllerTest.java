package me.jjeda.mall.orders.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.common.BaseControllerTest;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.items.domain.Item;
import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.items.repository.CategoryRepository;
import me.jjeda.mall.items.repository.ItemRepository;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.DeliveryStatus;
import me.jjeda.mall.orders.dto.DeliveryDto;
import me.jjeda.mall.orders.dto.OrderDto;
import me.jjeda.mall.orders.dto.OrderItemDto;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderControllerTest extends BaseControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ModelMapper modelMapper;


    @TestDescription("정상적으로 주문을 완료하는 테스트")
    @Test
    public void createOrder() throws Exception {
        //given
        //ItemDto -> OrderItemDto
        //DeliveryDto, OrderItemDto, Account -> OrderDto

        //판매자
        AccountDto sellerAccountDto = AccountDto.builder()
                .accountRole(Set.of(AccountRole.USER, AccountRole.SELLER))
                .address(new Address("a", "b", "c"))
                .email("seller@naver.com")
                .nickname("seller")
                .phone("01012341234")
                .password("pass")
                .build();
        Account seller = accountService.saveAccount(sellerAccountDto);

        //판매 상품등록
        ItemDto itemDto1 = ItemDto.builder()
                .account(seller)
                .name("상품1")
                .price(10000)
                .stockQuantity(100)
                .build();
        Item item1 = itemService.saveItem(itemDto1);

        ItemDto itemDto2 = ItemDto.builder()
                .account(seller)
                .name("상품2")
                .price(20000)
                .stockQuantity(200)
                .build();
        Item item2 = itemService.saveItem(itemDto2);

        //배송정보
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .address(new Address("a", "b", "c"))
                .build();

        //주문상품 정보
        OrderItemDto orderItemDto1 = OrderItemDto.builder()
                .build();
        OrderItemDto orderItemDto2 = OrderItemDto.builder()
                .build();

        //판매자 정보
        AccountDto buyerAccountDto = AccountDto.builder()
                .accountRole(Set.of(AccountRole.USER, AccountRole.SELLER))
                .address(new Address("a", "b", "c"))
                .email("buyer@naver.com")
                .nickname("buyer")
                .phone("01012341234")
                .password("pass")
                .build();
        Account buyer = accountService.saveAccount(buyerAccountDto);

        //주문정보
        OrderDto orderDto = OrderDto.builder()
                .deliveryDto(deliveryDto)
                .orderItemDtoList(List.of(orderItemDto1, orderItemDto2))
                .build();

        //when & then
        mockMvc.perform(post("/api/orders/buyer")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(buyerAccountDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsBytes(orderDto)))
                .andDo(print());


    }

    private String getAccessToken(AccountDto accountDto) throws Exception {
        // Given
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("temp", "pass"))
                .param("username", accountDto.getEmail())
                .param("password", accountDto.getPassword())
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return "bearer " + parser.parseMap(responseBody).get("access_token").toString();

    }


}