package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.CashPayment;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CashPaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CashPaymentService implements PaymentService {

    private final CashPaymentRepository cashPaymentRepository;
    private final OrderService orderService;
    private final ItemService itemService;

    @Override
    @Transactional
    public PaymentDto payForOrder(PaymentDto paymentDto, Long orderId) {
        //TODO : [#40] 탬플릿-콜백 패턴으로 공통부분 재사용코드로 만들기(각 결제 정보 저장로직만 다름)
        Order order = orderService.getOrder(orderId);
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

        CashPaymentDto cashPaymentDto = (CashPaymentDto) paymentDto;
        CashPayment cashPayment = cashPaymentDto.toEntity();
        cashPayment.setPayment(payment);
        cashPaymentRepository.save(cashPayment);

        return CashPaymentDto.toDto(cashPayment);
    }
}
