package me.jjeda.mall.orders.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    private int price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDateTime createdAt;

    /**
     * 주문이 완료되면 결제대기상태의 결제 Entity 를 만들기 위한 메서드
     */
    public static Payment toReadyEntity() {
        return Payment.builder()
                .paymentStatus(PaymentStatus.READY)
                .build();
    }

}
