package me.jjeda.mall.accounts.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;

    private String street;

    private String zipCode;
}
