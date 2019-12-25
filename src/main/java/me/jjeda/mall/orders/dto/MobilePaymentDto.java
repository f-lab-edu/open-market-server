package me.jjeda.mall.orders.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MobilePaymentDto extends PaymentDto {

    private Long mobilePaymentId;

    private String phone;

    private String telco;

    private String name;
}
