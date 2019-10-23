package me.jjeda.mall.orders.service;

import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.dto.DeliveryDto;
import me.jjeda.mall.orders.dto.OrderDto;
import me.jjeda.mall.orders.dto.OrderItemDto;
import me.jjeda.mall.orders.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ItemService itemService;

    private Account seller = AccountDto.builder()
            .accountRole(Set.of(AccountRole.USER, AccountRole.SELLER))
            .address(new Address("a", "b", "c"))
            .email("seller@naver.com")
            .nickname("seller")
            .phone("01012341234")
            .password("pass")
            .build()
            .toEntity();

    private Account buyer = AccountDto.builder()
            .accountRole(Set.of(AccountRole.USER))
            .address(new Address("a", "b", "c"))
            .email("buyer@naver.com")
            .nickname("buyer")
            .phone("01012341234")
            .password("pass")
            .build()
            .toEntity();

    private ItemDto itemDto1 = ItemDto.builder()
            .account(seller)
            .name("상품1")
            .price(10000)
            .stockQuantity(100)
            .build();

    private ItemDto itemDto2 = ItemDto.builder()
            .account(seller)
            .name("상품2")
            .price(20000)
            .stockQuantity(200)
            .build();

    private DeliveryDto deliveryDto = DeliveryDto.builder()
            .address(new Address("a", "b", "c"))
            .build();

    private OrderItemDto orderItemDto1 = OrderItemDto.builder()
            .itemDto(itemDto1)
            .quantity(2)
            .orderPrice(itemDto1.getPrice() * 2)
            .build();
    private OrderItemDto orderItemDto2 = OrderItemDto.builder()
            .itemDto(itemDto2)
            .quantity(3)
            .orderPrice(itemDto2.getPrice() * 3)
            .build();

    private OrderDto orderDto = OrderDto.builder()
            .deliveryDto(deliveryDto)
            .orderItemDtoList(List.of(orderItemDto1, orderItemDto2))
            .build();

    @Test
    @TestDescription("정상적으로 주문을 생성하는 테스트")
    public void createOrder() {

        //given
        Order order = orderDto.toEntity();
        order.setAccount(buyer);
        given(orderRepository.save(any())).willReturn(order);

        //when
        Order createOrder = orderService.createOrder(orderDto, buyer);

        //then
        assertThat(createOrder).isNotNull();
        assertThat(createOrder.getAccount()).isEqualTo(buyer);
        assertThat(createOrder.getDelivery().getOrder()).isEqualTo(createOrder);
        assertThat(createOrder.getOrderItems().get(0).getOrder()).isEqualTo(createOrder);
    }
}