package me.jjeda.mall.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.domain.PaymentType;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
public class PaymentDto {

    private Long id;

    private int price;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;

    private LocalDateTime createdAt;

    private PaymentDto superTypePaymentDto;
}
