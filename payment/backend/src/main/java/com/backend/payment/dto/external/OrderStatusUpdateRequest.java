package com.backend.payment.dto.external;

import lombok.Data;
import lombok.Builder;

/**
 * 다른 도메인 연동용 DTO
 * 주문 상태 변경 API 호출 시 사용
 */
@Data
@Builder
public class OrderStatusUpdateRequest {
    private String orderId;
    private String status;  // PAID, CANCELLED 등
    private String reason;
    private String paymentKey;
}