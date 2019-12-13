package me.jjeda.mall.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders/payment")
public class PaymentController {

    @PostMapping
    public ResponseEntity payForOrder() {

        //TODO : factory 패턴을 통해 client 에서 받아온 메시지의 PaymentType 에 따라 PaymentService 를 주입받는다.
        // final PaymentService paymentService = PaymentFactory.getType();
        return null;
    }
}
