package me.jjeda.mall.orders.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.orders.domain.MobilePayment;
import me.jjeda.mall.orders.domain.PaymentAdapter;
import me.jjeda.mall.orders.dto.MobilePaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.MobilePaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MobilePaymentService implements PaymentService {

    private final MobilePaymentRepository mobilePaymentRepository;

    @Override
    @Transactional
    public PaymentDto savePaymentInfo(PaymentDto paymentDto) {
        MobilePaymentDto mobilePaymentDto = (MobilePaymentDto) paymentDto;
        MobilePayment mobilePayment = PaymentAdapter.toEntity(mobilePaymentDto);
        mobilePaymentRepository.save(mobilePayment);

        return PaymentAdapter.toDto(mobilePayment);
    }
}
