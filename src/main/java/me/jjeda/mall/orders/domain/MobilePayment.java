package me.jjeda.mall.orders.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MobilePayment {

    @Id
    @GeneratedValue
    @Column(name = "mobile_payment_id")
    private Long id;

    private String phone;

    private String telco;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;
}
