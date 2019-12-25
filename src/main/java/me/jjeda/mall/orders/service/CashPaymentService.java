package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.CashPayment;
import me.jjeda.mall.orders.domain.PaymentAdapter;
import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CashPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CashPaymentService implements PaymentService {

    private final CashPaymentRepository cashPaymentRepository;

    @Override
    @Transactional
    public PaymentDto savePaymentInfo(PaymentDto paymentDto) {
        CashPaymentDto cashPaymentDto = (CashPaymentDto) paymentDto;
        CashPayment cashPayment = PaymentAdapter.toEntity(cashPaymentDto);
        cashPaymentRepository.save(cashPayment);

        return PaymentAdapter.toDto(cashPayment);
    }
}
