package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 내역 조회 DTO
 * GET /api/payments 응답의 개별 결제 정보
 */
@Data
@Builder
public class PaymentHistoryResponse {
    private String orderId;
    private BigDecimal amount;
    private String orderName;
    private String status;
    private String statusDescription;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    // 보안상 노출하지 않는 정보들:
    // - id (내부 관리번호)
    // - userId (개인정보)
    // - paymentKey (보안 정보)
    // - tossPaymentData (PG사 내부 데이터)
}