package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MobilePaymentDto extends PaymentDto {

    private Long mobilePaymentId;

    private String phone;

    private String telco;

    private String name;

    private PaymentDto paymentDto;
}
