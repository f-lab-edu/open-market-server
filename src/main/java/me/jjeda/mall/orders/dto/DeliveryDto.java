package me.jjeda.mall.orders.dto;

import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.orders.domain.Delivery;
import me.jjeda.mall.orders.domain.DeliveryStatus;

public class DeliveryDto {

    private Address address;

    private DeliveryStatus status;

    public Delivery from() {
        return Delivery.builder()
                .address(this.address)
                .status(DeliveryStatus.READY)
                .build();
    }
}
