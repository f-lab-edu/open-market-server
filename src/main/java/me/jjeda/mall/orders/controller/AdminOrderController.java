package me.jjeda.mall.orders.controller;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.Order;
import me.jjeda.mall.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}/delivery/start")
    public ResponseEntity startDelivery(@PathVariable Long orderId) {
        Order order = orderService.startDelivery(orderId);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/delivery/finish")
    public ResponseEntity finishDelivery(@PathVariable Long orderId) {
        Order order = orderService.finishDelivery(orderId);

        return ResponseEntity.ok(order);
    }
}
