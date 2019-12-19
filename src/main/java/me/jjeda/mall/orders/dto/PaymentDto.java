package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;
import me.jjeda.mall.orders.domain.Payment;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.domain.PaymentType;

@Builder
@Getter
public class PaymentDto {

    private Long id;

    private int price;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;

    public static Payment toReadyEntity() {
        return Payment.builder()
                .paymentStatus(PaymentStatus.READY)
                .build();
    }
}
