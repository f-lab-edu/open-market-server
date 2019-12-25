package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.CreditPayment;
import me.jjeda.mall.orders.domain.PaymentAdapter;
import me.jjeda.mall.orders.dto.CreditPaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CreditPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreditPaymentService implements PaymentService {

    private final CreditPaymentRepository creditPaymentRepository;

    @Override
    @Transactional
    public PaymentDto savePaymentInfo(PaymentDto paymentDto) {
        CreditPaymentDto creditPaymentDto = (CreditPaymentDto) paymentDto;
        CreditPayment creditPayment = PaymentAdapter.toEntity(creditPaymentDto);
        creditPaymentRepository.save(creditPayment);

        return PaymentAdapter.toDto(creditPayment);
    }

}
