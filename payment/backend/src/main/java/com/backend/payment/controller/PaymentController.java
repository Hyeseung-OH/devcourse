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
 * 결제 관련 API를 제공하는 REST 컨트롤러
 * 
 * 클라이언트(프론트엔드)와 결제 시스템 간의 HTTP 인터페이스를 담당합니다.
 * 토스페이먼츠 결제 연동을 위한 표준 REST API를 제공합니다.
 * 
 * 주요 기능:
 * - 결제 요청 생성 및 처리
 * - 결제 승인 및 실패 처리  
 * - 결제 내역 조회 (개인)
 * - 결제 상세 정보 조회
 * 
 * API 설계 원칙:
 * - RESTful URI 설계 (/api/payments)
 * - 표준 HTTP 상태 코드 사용
 * - 일관된 응답 형식 (ApiResponse 래퍼 사용)
 * - 상세한 로깅을 통한 디버깅 지원
 * - 예외 상황별 적절한 에러 응답
 * 
 * 보안 고려사항:
 * - CORS 설정 (현재는 개발용으로 전체 허용)
 * - 요청 데이터 검증 (향후 @Valid 추가 필요)
 * - 사용자 권한 검증 (향후 Security 연동 필요)
 * - 민감한 정보 로깅 제외
 * 
 * 향후 개선 사항:
 * - Spring Security를 통한 인증/인가 처리
 * - @Valid를 통한 입력값 검증 강화
 * - 전역 예외 처리기(GlobalExceptionHandler) 연동
 * - API 버전 관리 (/api/v1/payments)
 * - API 문서 자동화 (Swagger/OpenAPI)
 * - 속도 제한(Rate Limiting) 적용
 * 
 * @author Backend Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // TODO: 운영 환경에서는 특정 도메인만 허용하도록 변경 필요
public class PaymentController {

    private final PaymentService paymentService;


    /**
     * 결제 요청 생성 API
     * 
     * 클라이언트가 결제를 시작하기 위해 호출하는 첫 번째 API입니다.
     * 결제 정보를 데이터베이스에 저장하고 토스페이먼츠 결제창에 필요한 정보를 반환합니다.
     * 
     * @param request 결제 생성 요청 DTO
     *                - orderId: 주문 ID (선택적, 없으면 자동 생성)
     *                - userId: 결제 요청자 ID  
     *                - amount: 결제 금액
     *                - orderName: 주문명/상품명
     *                - customerName: 고객명
     *                - customerEmail: 고객 이메일 (선택적)
     *                - customerPhone: 고객 연락처 (선택적)
     * 
     * @return ResponseEntity<ApiResponse<PaymentCreateResponse>> 생성된 결제 정보
     * 
     * HTTP 메서드: POST
     * 요청 URL: /api/payments/request
     * Content-Type: application/json
     * 
     * 성공 응답 (200 OK):
     * {
     *   "success": true,
     *   "data": {
     *     "orderId": "ORDER_1703123456789_a1b2c3d4",
     *     "amount": 50000,
     *     "orderName": "아메리카노 2잔",
     *     "customerName": "홍길동",
     *     "createdAt": "2024-01-01T10:00:00"
     *   },
     *   "message": "결제 요청이 생성되었습니다"
     * }
     * 
     * 실패 응답:
     * - 409 Conflict: 중복 결제 요청
     * - 400 Bad Request: 잘못된 요청 데이터
     * - 500 Internal Server Error: 서버 내부 오류
     * 
     * 프론트엔드 연동 흐름:
     * 1. 이 API로 결제 요청 생성
     * 2. 응답받은 정보로 토스페이먼츠 결제창 호출
     * 3. 토스페이먼츠에서 결제 완료 후 confirm API 호출
     * 
     * 중복 결제 방지:
     * - 동일한 사용자, 주문, 금액의 조합으로 중복 요청 시 409 에러 반환
     * - 멱등성을 보장하여 네트워크 오류로 인한 중복 호출 방지
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> createPaymentRequest(
            @RequestBody PaymentCreateRequest request) { // TODO: @Valid 추가 필요

        log.info("=== 결제 요청 생성 API 시작 ===");
        log.info("요청 데이터 - orderId: {}, userId: {}, amount: {}, orderName: {}", 
                request.getOrderId(), request.getUserId(), request.getAmount(), request.getOrderName());

        try {
            // 비즈니스 로직은 서비스 계층에 위임
            PaymentCreateResponse response = paymentService.createPaymentRequest(request);

            log.info("결제 요청 생성 성공 - orderId: {}, paymentId 생성됨", response.getOrderId());
            
            // 표준 성공 응답 반환
            return ResponseEntity.ok(
                ApiResponse.success(response, "결제 요청이 생성되었습니다")
            );

        } catch (IllegalStateException e) {
            // 중복 결제 등의 비즈니스 규칙 위반
            log.warn("결제 요청 실패 - 비즈니스 규칙 위반: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.DUPLICATE_PAYMENT.getCode()));

        } catch (IllegalArgumentException e) {
            // 잘못된 입력 데이터
            log.warn("결제 요청 실패 - 잘못된 입력: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), PaymentErrorCode.INVALID_REQUEST.getCode()));

        } catch (Exception e) {
            // 예상하지 못한 시스템 오류
            log.error("결제 요청 API 시스템 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 요청 처리 중 오류가 발생했습니다", 
                                          PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 결제 승인 처리 API
     * 
     * 토스페이먼츠에서 결제 완료 후 클라이언트가 호출하는 API입니다.
     * 실제 결제 승인을 처리하고 결제 상태를 COMPLETED로 변경합니다.
     * 
     * @param request 결제 승인 요청 DTO
     *                - orderId: 주문 ID (결제 요청 시 생성된 ID)
     *                - paymentKey: 토스페이먼츠 결제 키
     *                - amount: 결제 금액 (위변조 검증용)
     * 
     * @return ResponseEntity<ApiResponse<PaymentConfirmResponse>> 승인 처리 결과
     * 
     * HTTP 메서드: POST
     * 요청 URL: /api/payments/confirm  
     * Content-Type: application/json
     * 
     * 성공 응답 (200 OK):
     * {
     *   "success": true,
     *   "data": {
     *     "success": true,
     *     "orderId": "ORDER_1703123456789_a1b2c3d4",
     *     "paymentKey": "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
     *     "amount": 50000,
     *     "status": "결제 완료",
     *     "message": "결제가 성공적으로 완료되었습니다",
     *     "approvedAt": "2024-01-01T10:05:30"
     *   },
     *   "message": "결제가 성공적으로 승인되었습니다"
     * }
     * 
     * 실패 응답:
     * - 400 Bad Request: 결제 정보 없음, 금액 불일치 등
     * - 409 Conflict: 이미 처리된 결제
     * - 500 Internal Server Error: 토스 API 오류, 시스템 오류
     * 
     * 보안 검증 사항:
     * - 결제 금액 위변조 검증 (요청 금액 vs DB 저장 금액)
     * - 결제 상태 검증 (PENDING 상태인지 확인)
     * - 토스페이먼츠 paymentKey 유효성 검증
     * 
     * 토스페이먼츠 연동 흐름:
     * 1. 클라이언트에서 이 API 호출
     * 2. 서버에서 토스페이먼츠 승인 API 호출
     * 3. 성공 시 DB 상태 업데이트
     * 4. 주문 시스템에 결제 완료 알림
     */
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResponse>> confirmPayment(
            @RequestBody PaymentConfirmRequest request) { // TODO: @Valid 추가 필요

        log.info("=== 결제 승인 처리 API 시작 ===");
        log.info("요청 데이터 - orderId: {}, paymentKey: {}, amount: {}", 
                request.getOrderId(), 
                maskPaymentKey(request.getPaymentKey()), // 보안을 위한 마스킹
                request.getAmount());

        try {
            // 결제 승인 비즈니스 로직 실행
            PaymentConfirmResponse response = paymentService.confirmPayment(request);

            // 서비스에서 반환된 결과에 따른 응답 분기
            if (response.isSuccess()) {
                log.info("결제 승인 성공 - orderId: {}, approvedAt: {}", 
                        request.getOrderId(), response.getApprovedAt());
                
                return ResponseEntity.ok(
                    ApiResponse.success(response, "결제가 성공적으로 승인되었습니다")
                );
            } else {
                // 서비스에서 실패 응답을 반환한 경우
                log.warn("결제 승인 실패 - orderId: {}, message: {}", 
                        request.getOrderId(), response.getMessage());
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(response.getMessage(), 
                                              PaymentErrorCode.TOSS_API_ERROR.getCode()));
            }

        } catch (IllegalArgumentException e) {
            // 결제 정보 없음, 잘못된 파라미터 등
            log.warn("결제 승인 실패 - 잘못된 요청: orderId={}, error={}", 
                    request.getOrderId(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), 
                                          PaymentErrorCode.PAYMENT_NOT_FOUND.getCode()));

        } catch (IllegalStateException e) {
            // 이미 처리된 결제, 취소된 결제 등 상태 오류
            log.warn("결제 승인 실패 - 상태 오류: orderId={}, error={}", 
                    request.getOrderId(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage(), 
                                          PaymentErrorCode.PAYMENT_ALREADY_CONFIRMED.getCode()));

        } catch (Exception e) {
            // 토스 API 오류, 시스템 오류 등
            log.error("결제 승인 API 시스템 오류 - orderId: {}", request.getOrderId(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("결제 승인 처리 중 오류가 발생했습니다", 
                                          PaymentErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    /**
     * 결제 키 마스킹 처리
     * 
     * 로그 출력 시 보안상 민감한 결제 키를 부분적으로 마스킹합니다.
     * 디버깅에는 도움이 되면서도 전체 키가 노출되지 않도록 합니다.
     * 
     * @param paymentKey 원본 결제 키
     * @return 마스킹 처리된 결제 키 (앞 10자리 + *** + 뒤 4자리)
     * 
     * 예시:
     * - 입력: "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6"
     * - 출력: "5zJ4xY7m0k***ZdL6"
     */
    private String maskPaymentKey(String paymentKey) {
        if (paymentKey == null || paymentKey.length() < 15) {
            return "***"; // 너무 짧은 경우 전체 마스킹
        }
        
        return paymentKey.substring(0, 10) + "***" + 
               paymentKey.substring(paymentKey.length() - 4);
    }
}
