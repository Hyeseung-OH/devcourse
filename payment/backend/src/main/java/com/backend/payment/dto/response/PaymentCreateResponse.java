package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 요청 생성 응답 DTO
 * POST /api/payments/request 응답
 */
@Data
@Builder
public class PaymentCreateResponse {
    private String orderId;
    private BigDecimal amount;
    private String orderName;
    private String customerName;
    private LocalDateTime createdAt;
}
