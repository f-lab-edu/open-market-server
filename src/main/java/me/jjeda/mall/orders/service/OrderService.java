package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.DeliveryStatus;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.OrderStatus;
import me.jjeda.mall.orders.dto.OrderDto;
import me.jjeda.mall.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    @Transactional
    public Order createOrder(OrderDto orderDto, AccountDto accountDto) {
        Order order = orderDto.toEntity();

        // 연관관계 메서드
        order.setAccount(accountDto.toEntity());
        //TODO : account.insertOrder(order);
        order.getDelivery().setOrder(order);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach((orderItem) -> orderItem.setOrder(order));

        /* 주문이 완료되면 아이템의 전체 재고에서 주문수량만큼 빼주어야한다. */
        //TODO : 벌크호출 + 결제가 완료되면 결제쪽에서 해주어야함
        orderItems.forEach((orderItem) ->
                itemService.decrementStock(orderItem.getItem().getId(), orderItem.getQuantity())
        );

        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
    }

    //TODO [#25] : 주문생성 -> 결제 -> 배송 에서 결제단계
    public Order payForOrder() {

        return null;
    }


    @Transactional
    public Order changeDeliveryStatus(Long orderId, DeliveryStatus deliveryStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        //TODO [#25] : 결제로직이 완성되면 결제상태를 체크하기
        order.getDelivery().setStatus(deliveryStatus);

        return order;
    }


    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        DeliveryStatus deliveryStatus = order.getDelivery().getStatus();
        if (Objects.equals(deliveryStatus, DeliveryStatus.DELIVERY) || Objects.equals(deliveryStatus, DeliveryStatus.COMP)) {
            throw new IllegalStateException("배송중이거나 배송완료된 상품은 취소가 불가능합니다.");
        }
        order.setStatus(OrderStatus.CANCEL);

        /* 주문을 취소하면 주문했던 수량만큼 다시 재고에 추가해주어야한다 */
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach((orderItem) ->
                itemService.incrementStock(orderItem.getItem().getId(), orderItem.getQuantity())
        );

        return order;
    }
}
