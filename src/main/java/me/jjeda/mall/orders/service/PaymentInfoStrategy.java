package me.jjeda.mall.orders.service;

import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.dto.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public interface PaymentInfoStrategy {
    PaymentDto savePaymentInfo(Payment payment);
}
