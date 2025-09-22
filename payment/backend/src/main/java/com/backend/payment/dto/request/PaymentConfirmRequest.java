package com.backend.payment.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 결제 승인 확인 DTO
 * 프론트엔드에서 POST /api/payments/confirm 호출 시 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private BigDecimal amount;
}