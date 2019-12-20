package me.jjeda.mall.orders.controller;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.dto.PaymentDto;
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

    private final PaymentService paymentService;

    @PostMapping(value = "/{orderId}")
    public ResponseEntity payForOrder(@RequestBody PaymentDto paymentDto, @PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.payForOrder(paymentDto, orderId));
    }

}
