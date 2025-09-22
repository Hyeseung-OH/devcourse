package com.backend.payment.dto.external;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 토스페이먼츠 API 응답 DTO
 * 토스에서 받은 응답을 매핑
 */
@Data
@Builder
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private BigDecimal totalAmount;
    private String method;
    private LocalDateTime approvedAt;
    private Map<String, Object> rawData;  // 전체 응답 데이터
}