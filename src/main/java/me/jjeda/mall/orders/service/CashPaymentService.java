package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CashPaymentRepository;
import me.jjeda.mall.orders.repository.PaymentRepository;

@RequiredArgsConstructor
public class CashPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CashPaymentRepository cashPaymentRepository;

    @Override
    public PaymentDto payForOrder(PaymentDto paymentDto, Long orderId) {
        return null;
    }
}
