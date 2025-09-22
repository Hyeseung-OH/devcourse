package com.backend.payment.controller;

import com.backend.payment.dto.request.PaymentCreateRequest;
import com.backend.payment.dto.request.PaymentConfirmRequest;
import com.backend.payment.dto.response.*;
import com.backend.payment.enums.PaymentErrorCode;
import com.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 결제 관련 API 컨트롤러
 * 프론트엔드와 연동되는 실제 API 엔드포인트
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // CORS 설정 (개발용, 실제론 특정 도메인만)
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 요청 생성
     * POST /api/payments/request
     * 프론트엔드에서 첫 번째로 호출하는 API
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> createPaymentRequest(
            @RequestBody PaymentCreateRequest request) {

        log.info("결제 요청 API 호출 - orderId: {}, amount: {}",
                request.getOrderId(), request.getAmount());

        try {
            PaymentCreateResponse response = paymentService.createPaymentRequest(request);

            log.info("결제 요청 API 성공 - orderId: {}", response.getOrderId());
            return ResponseEntity.ok(ApiResponse.success(response, "결제 요청이 생성되었습니다"));

        } catch (IllegalStateException e) {
            log.warn("결제 요청 실패 - 중복 결제: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.DUPLICATE_PAYMENT.getCode()));

        } catch (Exception e) {
            log.error("결제 요청 API 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 요청 처리 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 결제 승인 처리
     * POST /api/payments/confirm
     * 토스페이먼츠 결제 완료 후 프론트엔드에서 호출
     */
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResponse>> confirmPayment(
            @RequestBody PaymentConfirmRequest request) {

        log.info("결제 승인 API 호출 - orderId: {}, paymentKey: {}",
                request.getOrderId(), request.getPaymentKey());

        try {
            PaymentConfirmResponse response = paymentService.confirmPayment(request);

            if (response.isSuccess()) {
                log.info("결제 승인 API 성공 - orderId: {}", request.getOrderId());
                return ResponseEntity.ok(ApiResponse.success(response, "결제가 성공적으로 승인되었습니다"));
            } else {
                log.warn("결제 승인 실패 - orderId: {}, message: {}",
                        request.getOrderId(), response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(response.getMessage(), PaymentErrorCode.TOSS_API_ERROR.getCode()));
            }

        } catch (IllegalArgumentException e) {
            log.warn("결제 승인 실패 - 잘못된 요청: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.INVALID_REQUEST.getCode()));

        } catch (Exception e) {
            log.error("결제 승인 API 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 승인 처리 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 결제 내역 조회
     * GET /api/payments?userId={userId}
     * 마이페이지에서 사용자 결제 내역 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaymentListResponse>> getPaymentHistory(
            @RequestParam Long userId) {

        log.info("결제 내역 조회 API 호출 - userId: {}", userId);

        try {
            PaymentListResponse response = paymentService.getPaymentHistory(userId);

            log.info("결제 내역 조회 성공 - userId: {}, count: {}",
                    userId, response.getTotalCount());
            return ResponseEntity.ok(ApiResponse.success(response, "결제 내역을 조회했습니다"));

        } catch (Exception e) {
            log.error("결제 내역 조회 API 오류 - userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 내역 조회 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 결제 상세 조회
     * GET /api/payments/{orderId}
     * 특정 결제 건의 상세 정보 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<PaymentDetailResponse>> getPaymentDetail(
            @PathVariable String orderId) {

        log.info("결제 상세 조회 API 호출 - orderId: {}", orderId);

        try {
            PaymentDetailResponse response = paymentService.getPaymentDetail(orderId);

            log.info("결제 상세 조회 성공 - orderId: {}", orderId);
            return ResponseEntity.ok(ApiResponse.success(response, "결제 상세 정보를 조회했습니다"));

        } catch (IllegalArgumentException e) {
            log.warn("결제 상세 조회 실패 - orderId: {}, error: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.PAYMENT_NOT_FOUND.getCode()));

        } catch (Exception e) {
            log.error("결제 상세 조회 API 오류 - orderId: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 상세 조회 중 오류가 발생했습니다", PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }
}
