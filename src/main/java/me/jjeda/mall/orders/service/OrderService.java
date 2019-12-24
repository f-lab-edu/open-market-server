package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.accounts.domain.AccountAndDtoAdapter;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.CashPayment;
import me.jjeda.mall.orders.domain.DeliveryStatus;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.OrderStatus;
import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.OrderDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
        order.setAccount(AccountAndDtoAdapter.dtoToEntity(accountDto));
        //TODO : [#33] Account <-> Order 연관관계 메서드 처리
        // order.getAccount().insertOrder(order);
        order.getDelivery().setOrder(order);
        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = 0;
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            totalPrice += item.getOrderPrice() * item.getQuantity();
        }
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
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

        //TODO : 주문 취소 ->결제 취소 할때로 변경
        /* 주문을 취소하면 주문했던 수량만큼 다시 재고에 추가해주어야한다 */
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach((orderItem) ->
                itemService.incrementStock(orderItem.getItem().getId(), orderItem.getQuantity())
        );

        return order;
    }

    @Transactional
    public PaymentDto completePayment(PaymentService paymentService, PaymentDto paymentDto, Long orderId) {
        Order order = getOrder(orderId);
        Payment payment = order.getPayment();
        List<OrderItem> orderItems = order.getOrderItems();

        payment = Payment.builder()
                .paymentStatus(PaymentStatus.COMP)
                .id(payment.getId())
                .price(order.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .paymentType(paymentDto.getPaymentType())
                .build();

        /* 주문이 완료되면 아이템의 전체 재고에서 주문수량만큼 빼주어야한다. */
        //TODO : N+1문제 -> 벌크호출
        orderItems.forEach((orderItem) ->
                itemService.decrementStock(orderItem.getItem().getId(), orderItem.getQuantity())
        );
        return paymentService.savePaymentInfo(paymentDto, payment);
    }
}
