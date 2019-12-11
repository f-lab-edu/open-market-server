package me.jjeda.mall.orders.repository;

import me.jjeda.mall.orders.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
