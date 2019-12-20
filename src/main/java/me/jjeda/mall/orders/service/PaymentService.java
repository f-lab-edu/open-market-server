package me.jjeda.mall.orders.service;

import me.jjeda.mall.orders.dto.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    PaymentDto payForOrder(PaymentDto paymentDto, Long orderId);

}
