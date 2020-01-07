package me.jjeda.mall.orders.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CreditPaymentDto extends PaymentDto {

    private Long creditPaymentId;

    private String bank;

    private String cardNumber;

    private String name;
}
