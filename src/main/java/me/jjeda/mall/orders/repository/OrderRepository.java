package me.jjeda.mall.orders.repository;

import me.jjeda.mall.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
