package me.jjeda.mall.orders.service;

import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.DeliveryStatus;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderStatus;
import me.jjeda.mall.orders.dto.DeliveryDto;
import me.jjeda.mall.orders.dto.OrderDto;
import me.jjeda.mall.orders.dto.OrderItemDto;
import me.jjeda.mall.orders.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        given(orderRepository.save(any())).willReturn(orderDto.toEntity());

        //when
        orderService.createOrder(orderDto, buyer);

        //then
        verify(orderRepository, times(1)).save(
                argThat(innerOrder -> innerOrder.getAccount().equals(buyer)
                        && innerOrder.getDelivery().getOrder().equals(innerOrder)
                        && innerOrder.getOrderItems().get(0).getOrder().equals(innerOrder)));
    }

    @Test
    @TestDescription("정상적으로 주문정보 불러오는 테스트")
    public void getOrder() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        Order getOrder = orderService.getOrder(1L);

        assertThat(getOrder.getOrderItems()).isEqualTo(order.getOrderItems());
        assertThat(getOrder.getDelivery()).isEqualTo(order.getDelivery());
    }

    @Test(expected = EntityNotFoundException.class)
    @TestDescription("없는 주문정보를 불러올때 예외처리하는 테스트")
    public void getOrder_Throw_Entity_Not_Found() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        //when
        orderService.getOrder(1L);
    }

    @Test
    @TestDescription("정상적으로 배송상태를 변경하는 테스트")
    public void changeDeliveryStatus() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        orderService.changeDeliveryStatus(1L, DeliveryStatus.DELIVERY);

        //then
        assertThat(order.getDelivery().getStatus()).isEqualTo(DeliveryStatus.DELIVERY);
    }

    @Test(expected = EntityNotFoundException.class)
    @TestDescription("없는 주문정보의 배송상태를 변경할 때 예외가 발생하는 테스트")
    public void changeDeliveryStatus_Throw_Entity_Not_Found() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        //when
        orderService.changeDeliveryStatus(1L, DeliveryStatus.DELIVERY);
    }

    @Test
    @TestDescription("정상적으로 주문을 취소하는 테스트")
    public void cancelOrder() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        orderService.cancelOrder(1L);

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test(expected = EntityNotFoundException.class)
    @TestDescription("없는 주문정보의 주문을 취소할 때 예외가 발생하는 테스트")
    public void cancelOrder_Throw_Entity_Not_Found() {
        //given
        Order order = orderDto.toEntity();
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        //when
        orderService.cancelOrder(1L);
    }

    @Test(expected = IllegalStateException.class)
    @TestDescription("배송준비 상태일 때 예외가 발생하는 테스트")
    public void cancelOrder_Throw_Illegal_State() {
        //given
        Order order = orderDto.toEntity();
        order.getDelivery().setStatus(DeliveryStatus.DELIVERY);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        orderService.cancelOrder(1L);
    }

}