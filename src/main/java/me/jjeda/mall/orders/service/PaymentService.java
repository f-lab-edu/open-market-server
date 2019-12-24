package me.jjeda.mall.orders.service;

import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.dto.PaymentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface PaymentService {
    PaymentDto savePaymentInfo(PaymentDto paymentDto, Payment payment);
}
