package me.jjeda.mall.orders.service;

import me.jjeda.mall.common.TestDescription;
import me.jjeda.mall.orders.domain.CashPayment;
import me.jjeda.mall.orders.domain.PaymentAdapter;
import me.jjeda.mall.orders.domain.PaymentStatus;
import me.jjeda.mall.orders.domain.PaymentType;
import me.jjeda.mall.orders.dto.CashPaymentDto;
import me.jjeda.mall.orders.dto.PaymentDto;
import me.jjeda.mall.orders.repository.CashPaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @InjectMocks
    private CashPaymentService cashPaymentService;

    @Mock
    private CashPaymentRepository cashPaymentRepository;

    @Test
    @TestDescription("정상적으로 현금결제정보 저장하는 테스트")
    public void savePaymentInfo_cash() {

        //given
        PaymentDto superPaymentDto = PaymentDto.builder()
                .id(1L)
                .paymentStatus(PaymentStatus.COMP)
                .paymentType(PaymentType.CASH)
                .price(10000)
                .build();
        CashPaymentDto cashPaymentDto = CashPaymentDto.builder()
                .cashPaymentId(2L)
                .name("jjeda")
                .bank("kakao")
                .bankAccount("1234")
                .superTypePaymentDto(superPaymentDto)
                .build();

        //when
        PaymentDto returnDto = cashPaymentService.savePaymentInfo(cashPaymentDto);
        CashPaymentDto returnCashPaymentDto = (CashPaymentDto)returnDto;

        assertThat(returnCashPaymentDto.getCashPaymentId()).isEqualTo(2L);
        assertThat(returnCashPaymentDto.getName()).isEqualTo("jjeda");
        assertThat(returnCashPaymentDto.getBank()).isEqualTo("kakao");
        assertThat(returnCashPaymentDto.getBankAccount()).isEqualTo("1234");
    }

}
