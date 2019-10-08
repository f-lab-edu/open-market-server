package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.items.domain.Item;
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
    public Order createOrder(OrderDto orderDto, Account account) {
        Order order = orderDto.from();

        // 연관관계 메서드
        order.setAccount(account);
        account.getOrders().add(order);
        order.getDelivery().setOrder(order);
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        /* 주문이 완료되면 아이템의 전체 재고에서 주문수량만큼 빼주어야한다. */
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            itemService.removeStock(item.getId(), orderItem.getQuantity());
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
    }

    //TODO [#25] : 주문생성 -> 결제 -> 배송 에서 결제단계
    public Order payForOrder() {

        return null;
    }


    /* 배송상태는 준비 -> 배송중 -> 배송완료 단방향이므로 status 를 파라미터로하여 상태를 변경하는 메서드가아니라
     * 준비 -> 배송중  / 배송중 -> 완료 메서드로 분리
     */
    @Transactional
    public Order startDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        //TODO [#25] : 결제로직이 완성되면 결제상태를 체크하기
        order.getDelivery().setStatus(DeliveryStatus.DELIVERY);

        return order;
    }

    @Transactional
    public Order finishDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.getDelivery().setStatus(DeliveryStatus.COMP);

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
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            itemService.addStock(item.getId(), orderItem.getQuantity());
        }

        return order;
    }
}