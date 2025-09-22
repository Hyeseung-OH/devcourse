package com.backend.payment.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 결제 요청 생성 DTO
 * 프론트엔드에서 POST /api/payments/request 호출 시 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {
    private BigDecimal amount;
    private String orderName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String orderId;     // 다른 도메인에서 전달받음
    private Long userId;        // 다른 도메인에서 전달받음
}