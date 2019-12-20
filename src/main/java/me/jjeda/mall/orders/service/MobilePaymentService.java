package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.MobilePaymentRepository;
import me.jjeda.mall.orders.repository.PaymentRepository;

@RequiredArgsConstructor
public class MobilePaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MobilePaymentRepository mobilePaymentRepository;

    @Override
    public PaymentDto payForOrder(PaymentDto paymentDto, Long orderId) {
        return null;
    }
}
