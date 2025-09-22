package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 상세 조회 DTO
 * GET /api/payments/{orderId} 응답
 */
@Data
@Builder
public class PaymentDetailResponse {
    private String orderId;
    private BigDecimal amount;
    private String orderName;
    private String customerName;
    private String customerEmail;
    private String status;
    private String statusDescription;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    // 취소 관련 정보 (있을 경우에만)
    private String cancelReason;
    private BigDecimal cancelAmount;
    private LocalDateTime cancelledAt;
}