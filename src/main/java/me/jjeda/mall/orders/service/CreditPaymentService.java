package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CreditPaymentRepository;
import me.jjeda.mall.orders.repository.PaymentRepository;

@RequiredArgsConstructor
public class CreditPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditPaymentRepository creditPaymentRepository;

    @Override
    public PaymentDto payForOrder(PaymentDto paymentDto, Long orderId) {
        return null;
    }

}
