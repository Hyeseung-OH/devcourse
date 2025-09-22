package com.backend.payment.dto.response;

import lombok.Data;
import lombok.Builder;

import java.util.List;

/**
 * 결제 내역 목록 응답 DTO
 * GET /api/payments 전체 응답
 */
@Data
@Builder
public class PaymentListResponse {
    private List<PaymentHistoryResponse> payments;
    private int totalCount;
    private boolean hasMore;  // 페이징을 위한 정보
}