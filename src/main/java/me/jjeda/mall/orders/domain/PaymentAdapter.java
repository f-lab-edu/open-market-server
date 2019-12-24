package me.jjeda.mall.orders.domain;

import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.CreditPaymentDto;
import me.jjeda.mall.orders.dto.MobilePaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;

public class PaymentAdapter {

    public static CashPayment toEntity(CashPaymentDto cashPaymentDto) {
        return CashPayment.builder()
                .id(cashPaymentDto.getCashPaymentId())
                .bank(cashPaymentDto.getBank())
                .bankAccount(cashPaymentDto.getBankAccount())
                .name(cashPaymentDto.getName())
                .build();
    }

    public static CreditPayment toEntity(CreditPaymentDto creditPaymentDto) {
        return CreditPayment.builder()
                .id(creditPaymentDto.getCreditPaymentId())
                .bank(creditPaymentDto.getBank())
                .cardNumber(creditPaymentDto.getCardNumber())
                .name(creditPaymentDto.getName())
                .build();
    }

    public static MobilePayment toEntity(MobilePaymentDto mobilePaymentDto) {
        return MobilePayment.builder()
                .id(mobilePaymentDto.getMobilePaymentId())
                .phone(mobilePaymentDto.getPhone())
                .telco(mobilePaymentDto.getTelco())
                .name(mobilePaymentDto.getName())
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
                .paymentDto(toDto(cashPayment.getPayment()))
                .build();
    }

    public static PaymentDto toDto(CreditPayment creditPayment) {
        return CreditPaymentDto.builder()
                .creditPaymentId(creditPayment.getId())
                .bank(creditPayment.getBank())
                .cardNumber(creditPayment.getCardNumber())
                .name(creditPayment.getName())
                .paymentDto(toDto(creditPayment.getPayment()))
                .build();
    }

    public static PaymentDto toDto(MobilePayment mobilePayment) {
        return MobilePaymentDto.builder()
                .mobilePaymentId(mobilePayment.getId())
                .phone(mobilePayment.getPhone())
                .telco(mobilePayment.getTelco())
                .name(mobilePayment.getName())
                .paymentDto(toDto(mobilePayment.getPayment()))
                .build();
    }
}
