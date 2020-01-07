package me.jjeda.mall.orders.domain;

import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.CreditPaymentDto;
import me.jjeda.mall.orders.dto.MobilePaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;

public class PaymentAdapter {
    public static Payment toEntity(PaymentDto paymentDto) {
        return Payment.builder()
                .id(paymentDto.getId())
                .price(paymentDto.getPrice())
                .createdAt(paymentDto.getCreatedAt())
                .paymentType(paymentDto.getPaymentType())
                .paymentStatus(paymentDto.getPaymentStatus())
                .build();
    }

    public static CashPayment toEntity(CashPaymentDto cashPaymentDto) {
        return CashPayment.builder()
                .id(cashPaymentDto.getCashPaymentId())
                .bank(cashPaymentDto.getBank())
                .bankAccount(cashPaymentDto.getBankAccount())
                .name(cashPaymentDto.getName())
                .payment(toEntity(cashPaymentDto.getSuperTypePaymentDto()))
                .build();
    }

    public static CreditPayment toEntity(CreditPaymentDto creditPaymentDto) {
        return CreditPayment.builder()
                .id(creditPaymentDto.getCreditPaymentId())
                .bank(creditPaymentDto.getBank())
                .cardNumber(creditPaymentDto.getCardNumber())
                .name(creditPaymentDto.getName())
                .payment(toEntity(creditPaymentDto.getSuperTypePaymentDto()))
                .build();
    }

    public static MobilePayment toEntity(MobilePaymentDto mobilePaymentDto) {
        return MobilePayment.builder()
                .id(mobilePaymentDto.getMobilePaymentId())
                .phone(mobilePaymentDto.getPhone())
                .telco(mobilePaymentDto.getTelco())
                .name(mobilePaymentDto.getName())
                .payment(toEntity(mobilePaymentDto.getSuperTypePaymentDto()))
                .build();
    }

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .price(payment.getPrice())
                .paymentStatus(payment.getPaymentStatus())
                .paymentType(payment.getPaymentType())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public static PaymentDto toDto(CashPayment cashPayment) {
        return CashPaymentDto.builder()
                .cashPaymentId(cashPayment.getId())
                .bank(cashPayment.getBank())
                .bankAccount(cashPayment.getBankAccount())
                .name(cashPayment.getName())
                .build();
    }

    public static PaymentDto toDto(CreditPayment creditPayment) {
        return CreditPaymentDto.builder()
                .creditPaymentId(creditPayment.getId())
                .bank(creditPayment.getBank())
                .cardNumber(creditPayment.getCardNumber())
                .name(creditPayment.getName())
                .build();
    }

    public static PaymentDto toDto(MobilePayment mobilePayment) {
        return MobilePaymentDto.builder()
                .mobilePaymentId(mobilePayment.getId())
                .phone(mobilePayment.getPhone())
                .telco(mobilePayment.getTelco())
                .name(mobilePayment.getName())
                .build();
    }
}
