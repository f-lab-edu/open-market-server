package me.jjeda.mall.orders.domain;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.service.CashPaymentService;
import me.jjeda.mall.orders.service.CreditPaymentService;
import me.jjeda.mall.orders.service.MobilePaymentService;
import me.jjeda.mall.orders.service.PaymentService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final CashPaymentService cashPaymentService;
    private final CreditPaymentService creditPaymentService;
    private final MobilePaymentService mobilePaymentService;

    public PaymentService getType(PaymentType paymentType) {
        final PaymentService paymentService;

        switch (paymentType) {
            case CASH:
                paymentService = cashPaymentService;
                break;
            case CREDIT:
                paymentService = creditPaymentService;
                break;
            case MOBILE:
                paymentService = mobilePaymentService;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return paymentService;
    }
}
