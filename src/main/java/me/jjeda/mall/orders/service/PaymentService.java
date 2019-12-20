package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.items.service.ItemService;
import me.jjeda.mall.orders.domain.CashPayment;
import me.jjeda.mall.orders.domain.CreditPayment;
import me.jjeda.mall.orders.domain.MobilePayment;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.domain.OrderItem;
import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.CreditPaymentDto;
import me.jjeda.mall.orders.dto.MobilePaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CashPaymentRepository;
import me.jjeda.mall.orders.repository.CreditPaymentRepository;
import me.jjeda.mall.orders.repository.MobilePaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final ItemService itemService;
    private final CashPaymentRepository cashPaymentRepository;
    private final CreditPaymentRepository creditPaymentRepository;
    private final MobilePaymentRepository mobilePaymentRepository;


    @Transactional
    public PaymentDto payForOrderWithStrategy(PaymentInfoStrategy paymentInfoStrategy,
                                              PaymentDto paymentDto, Long orderId) {
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
        return paymentInfoStrategy.savePaymentInfo(payment);
    }

    /**
     * 결제 방법을 결정하는 역할
     * 기존의 Service 를 선택하는 PaymentFactory 를 대체
     */
    public PaymentDto payForOrder(PaymentDto paymentDto, Long orderId) {
        switch (paymentDto.getPaymentType()) {
            case CASH:
                return payForOrderWithStrategy(payment -> {
                    CashPaymentDto cashPaymentDto = (CashPaymentDto) paymentDto;
                    CashPayment cashPayment = cashPaymentDto.toEntity();
                    cashPayment.setPayment(payment);
                    cashPaymentRepository.save(cashPayment);
                    return CashPaymentDto.toDto(cashPayment);
                }, paymentDto, orderId);
            case CREDIT:
                return payForOrderWithStrategy(payment -> {
                    CreditPaymentDto creditPaymentDto = (CreditPaymentDto) paymentDto;
                    CreditPayment creditPayment = creditPaymentDto.toEntity();
                    creditPayment.setPayment(payment);
                    creditPaymentRepository.save(creditPayment);
                    return CreditPaymentDto.toDto(creditPayment);
                }, paymentDto, orderId);
            case MOBILE:
                return payForOrderWithStrategy(payment -> {
                    MobilePaymentDto mobilePaymentDto = (MobilePaymentDto) paymentDto;
                    MobilePayment mobilePayment = mobilePaymentDto.toEntity();
                    mobilePayment.setPayment(payment);
                    mobilePaymentRepository.save(mobilePayment);
                    return MobilePaymentDto.toDto(mobilePayment);
                }, paymentDto, orderId);
            default:
                throw new IllegalArgumentException();
        }
    }
}
