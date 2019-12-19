package me.jjeda.mall.orders.service;

import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.dto.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    Payment payForOrder(PaymentDto paymentDto, Long orderId);

}
