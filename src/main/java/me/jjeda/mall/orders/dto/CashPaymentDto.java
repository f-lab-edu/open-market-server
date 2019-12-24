package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CashPaymentDto extends PaymentDto {

    private Long cashPaymentId;

    private String bank;

    private String bankAccount;

    private String name;

    private PaymentDto paymentDto;
}
