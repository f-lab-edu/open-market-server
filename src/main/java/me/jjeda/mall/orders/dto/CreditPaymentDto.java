package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditPaymentDto extends PaymentDto {

    private Long creditPaymentId;

    private String bank;

    private String cardNumber;

    private String name;

    private PaymentDto paymentDto;
}
