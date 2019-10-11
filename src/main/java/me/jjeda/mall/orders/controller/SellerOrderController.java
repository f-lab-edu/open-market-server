package me.jjeda.mall.orders.controller;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.DeliveryStatus;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders/seller")
public class SellerOrderController {

    private final OrderService orderService;

    @PatchMapping("/{orderId}/delivery")
    public ResponseEntity changeDeliveryStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus deliveryStatus) {
        Order order = orderService.changeDeliveryStatus(orderId, deliveryStatus);

        return ResponseEntity.ok(order);
    }

}
