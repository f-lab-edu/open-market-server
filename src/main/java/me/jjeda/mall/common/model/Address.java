package me.jjeda.mall.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
 * reflection, proxy 등을 위해 기본 생성자를 만들어주어야한다.
 * JPA 스펙에서는 Protected 까지 허용
 */

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String city;

    private String street;

    private String zipCode;
}
