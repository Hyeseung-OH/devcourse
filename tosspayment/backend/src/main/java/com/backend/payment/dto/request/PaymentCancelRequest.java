package com.backend.payment.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 결제 취소 요청 DTO
 * 관리자가 POST /admin/payments/{orderId}/cancel 호출 시 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequest {
    private String cancelReason;
    private BigDecimal cancelAmount;  // null이면 전액 취소
}