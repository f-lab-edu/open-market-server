package me.jjeda.mall.orders.repository;

import me.jjeda.mall.orders.domain.MobilePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobilePaymentRepository extends JpaRepository<MobilePayment, Long> {
}
