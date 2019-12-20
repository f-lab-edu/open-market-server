package me.jjeda.mall.orders.dto;

import me.jjeda.mall.orders.domain.CashPayment;

public class CashPaymentDto extends PaymentDto {

    private Long cashPaymentId;

    private String bank;

    private String bankAccount;

    private String name;

    private PaymentDto paymentDto;

    public CashPayment toEntity() {
        return CashPayment.builder()
                .id(this.cashPaymentId)
                .bank(this.bank)
                .bankAccount(this.bankAccount)
                .name(this.name)
                .payment(paymentDto.toPaymentEntity())
                .build();
    }
    public static PaymentDto toDto(CashPayment cashPayment) {
        return CashPaymentDto.builder()
                .build();
    }
}
