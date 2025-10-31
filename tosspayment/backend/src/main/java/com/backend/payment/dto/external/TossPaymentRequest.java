package com.backend.payment.dto.external;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * 토스페이먼츠 API 호출용 DTO
 * 외부 PG사 연동 시 사용
 */
@Data
@Builder
public class TossPaymentRequest {
    private String paymentKey;
    private String orderId;
    private BigDecimal amount;
}
