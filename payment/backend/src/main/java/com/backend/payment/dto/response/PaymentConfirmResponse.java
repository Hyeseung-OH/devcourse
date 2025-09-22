package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 승인 결과 DTO
 * POST /api/payments/confirm 응답
 */
@Data
@Builder
public class PaymentConfirmResponse {
    private boolean success;
    private String orderId;
    private String paymentKey;
    private BigDecimal amount;
    private String status;
    private String message;
    private LocalDateTime approvedAt;
}