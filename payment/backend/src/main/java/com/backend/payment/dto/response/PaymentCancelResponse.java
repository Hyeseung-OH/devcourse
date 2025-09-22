package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 취소 결과 DTO
 * POST /admin/payments/{orderId}/cancel 응답
 */
@Data
@Builder
public class PaymentCancelResponse {
    private boolean success;
    private String orderId;
    private BigDecimal cancelAmount;
    private String message;
    private LocalDateTime cancelledAt;
}