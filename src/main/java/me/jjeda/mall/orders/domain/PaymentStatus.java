package me.jjeda.mall.orders.domain;

/**
 * 결제상태를 나타내는 enum
 * READY : 결제전
 * COMP :결제완료
 * CANCEL : 결제취소
 * REFUND : 환불
 */
public enum PaymentStatus {
    READY, COMP, CANCEL, REFUND
}
