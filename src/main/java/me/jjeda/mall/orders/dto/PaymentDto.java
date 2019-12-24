package me.jjeda.mall.orders.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.domain.PaymentType;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PaymentDto {

    private Long id;

    private int price;

    private PaymentStatus paymentStatus;

    private PaymentType paymentType;

    private LocalDateTime createdAt;
}
