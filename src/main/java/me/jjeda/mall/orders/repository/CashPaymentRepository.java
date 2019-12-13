package me.jjeda.mall.orders.repository;

import me.jjeda.mall.orders.domain.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashPaymentRepository extends JpaRepository<CashPayment, Long> {
}
