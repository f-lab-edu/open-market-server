package me.jjeda.mall.orders.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.common.model.Address;
import me.jjeda.mall.orders.domain.Delivery;
import me.jjeda.mall.orders.domain.DeliveryStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDto {

    private Address address;

    public Delivery toEntity() {
        return Delivery.builder()
                .address(this.address)
                .status(DeliveryStatus.READY)
                .build();
    }
}
