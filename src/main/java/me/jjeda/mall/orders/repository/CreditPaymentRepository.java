package me.jjeda.mall.orders.repository;

import me.jjeda.mall.orders.domain.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, Long> {

}
