package me.jjeda.mall.orders.domain;

import me.jjeda.mall.orders.service.CashPaymentService;
import me.jjeda.mall.orders.service.CreditPaymentService;
import me.jjeda.mall.orders.service.MobilePaymentService;
import me.jjeda.mall.orders.service.PaymentService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentFactory {

    private final Map<PaymentType, PaymentService> map;

    public PaymentFactory(CashPaymentService cashPaymentService, CreditPaymentService creditPaymentService,
                          MobilePaymentService mobilePaymentService) {
        map = new HashMap<>();
        map.put(PaymentType.CASH, cashPaymentService);
        map.put(PaymentType.CREDIT, creditPaymentService);
        map.put(PaymentType.MOBILE, mobilePaymentService);
    }

    public PaymentService getType(PaymentType paymentType) {
        if(map.containsKey(paymentType)) {
            return map.get(paymentType);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
