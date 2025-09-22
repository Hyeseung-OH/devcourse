package com.backend.payment.controller;

import com.backend.payment.dto.request.PaymentCancelRequest;
import com.backend.payment.dto.response.ApiResponse;
import com.backend.payment.dto.response.PaymentCancelResponse;
import com.backend.payment.dto.response.PaymentListResponse;
import com.backend.payment.enums.PaymentErrorCode;
import com.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 결제 API 컨트롤러
 * 일반 사용자 API와 분리하여 관리
 */
@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminPaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 취소/환불 (관리자용)
     * POST /api/admin/payments/{orderId}/cancel
     * 최병준님 관리자 화면에서 호출
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<PaymentCancelResponse>> cancelPayment(
            @PathVariable String orderId,
            @RequestBody PaymentCancelRequest request) {

        log.info("관리자 결제 취소 API 호출 - orderId: {}, reason: {}",
                orderId, request.getCancelReason());

        try {
            PaymentCancelResponse response = paymentService.cancelPayment(orderId, request);

            if (response.isSuccess()) {
                log.info("관리자 결제 취소 성공 - orderId: {}", orderId);
                return ResponseEntity.ok(ApiResponse.success(response, "결제가 성공적으로 취소되었습니다"));
            } else {
                log.warn("관리자 결제 취소 실패 - orderId: {}, message: {}",
                        orderId, response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(response.getMessage(), PaymentErrorCode.TOSS_API_ERROR.getCode()));
            }

        } catch (IllegalArgumentException e) {
            log.warn("관리자 결제 취소 실패 - 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.INVALID_REQUEST.getCode()));

        } catch (IllegalStateException e) {
            log.warn("관리자 결제 취소 실패 - 상태 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.PAYMENT_CANCELLED.getCode()));

        } catch (Exception e) {
            log.error("관리자 결제 취소 API 오류 - orderId: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 취소 처리 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 관리자용 결제 내역 조회
     * GET /api/admin/payments
     * 모든 결제 내역 조회 (관리자 전용)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaymentListResponse>> getAllPaymentHistory(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {

        log.info("관리자 전체 결제 내역 조회 API 호출 - status: {}, page: {}, size: {}",
                status, page, size);

        try {
            // TODO: 관리자용 전체 결제 내역 조회 서비스 메서드 구현 필요
            // PaymentListResponse response = paymentService.getAllPaymentHistory(status, page, size);

            // 임시 응답
            PaymentListResponse response = PaymentListResponse.builder()
                    .payments(java.util.List.of())
                    .totalCount(0)
                    .hasMore(false)
                    .build();

            return ResponseEntity.ok(ApiResponse.success(response, "관리자 결제 내역을 조회했습니다"));

        } catch (Exception e) {
            log.error("관리자 전체 결제 내역 조회 API 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("관리자 결제 내역 조회 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }
}