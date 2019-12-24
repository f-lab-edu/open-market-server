package me.jjeda.mall.orders.controller;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.PaymentFactory;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.service.OrderService;
import me.jjeda.mall.orders.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFactory paymentFactory;
    private final OrderService orderService;

    @PostMapping(value = "/{orderId}")
    public ResponseEntity completePayment(@RequestBody PaymentDto paymentDto, @PathVariable Long orderId) {
        final PaymentService paymentService = paymentFactory.getType(paymentDto.getPaymentType());
        return ResponseEntity.ok(orderService.completePayment(paymentService, paymentDto, orderId));
    }
}
