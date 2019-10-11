package me.jjeda.mall.orders.dto;

import lombok.Builder;
import lombok.Getter;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.orders.domain.Delivery;
import me.jjeda.mall.orders.domain.DeliveryStatus;

@Getter
@Builder
public class DeliveryDto {

    private Address address;

    private DeliveryStatus status;

    public Delivery to() {
        return Delivery.builder()
                .address(this.address)
                .status(DeliveryStatus.READY)
                .build();
    }
}
