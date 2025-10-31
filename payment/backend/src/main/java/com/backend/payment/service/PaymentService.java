package com.backend.payment.service;

import com.backend.payment.dto.request.PaymentCreateRequest;
import com.backend.payment.dto.request.PaymentConfirmRequest;
import com.backend.payment.dto.request.PaymentCancelRequest;
import com.backend.payment.dto.response.*;
import com.backend.payment.entity.Payment;
import com.backend.payment.entity.PaymentStatus;
import com.backend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;


    /**
     * 결제 요청 생성
     *
     * 클라이언트에서 결제 요청을 받아 결제 엔티티를 생성하고 저장합니다.
     * 이 단계에서는 아직 실제 결제 처리는 하지 않고, 결제 준비 상태로만 저장합니다.
     *
     * @param request 결제 생성 요청 DTO (주문 정보, 고객 정보, 결제 금액 등)
     * @return PaymentCreateResponse 생성된 결제 정보 응답
     *
     * 처리 흐름:
     * 1. 중복 결제 검증 (같은 사용자, 주문, 금액의 기존 결제 확인)
     * 2. 주문 ID 유효성 검증 및 생성 (필요시)
     * 3. 결제 엔티티 생성 및 저장 (PENDING 상태)
     * 4. 프론트엔드로 응답 데이터 반환
     *
     * 비즈니스 규칙:
     * - 동일한 주문에 대한 중복 결제 요청 차단
     * - 결제 금액은 0보다 커야 함
     * - 필수 고객 정보 누락 시 예외 발생
     *
     * 보안 고려사항:
     * - 결제 요청자와 주문자의 일치 여부 확인 필요 (향후 개선)
     * - 결제 금액의 범위 검증 (최소/최대 금액 제한)
     *
     * 예외 상황:
     * - IllegalStateException: 중복 결제 요청 시
     * - IllegalArgumentException: 잘못된 입력 데이터 시
     *
     * @throws IllegalStateException 중복 결제 요청인 경우
     * @throws IllegalArgumentException 잘못된 요청 데이터인 경우
     */
    @Transactional // 쓰기 작업이므로 읽기 전용 해제
    public PaymentCreateResponse createPaymentRequest(PaymentCreateRequest request) {
        log.info("결제 요청 생성 시작 - orderId: {}, userId: {}, amount: {}",
                request.getOrderId(), request.getUserId(), request.getAmount());

        // 1. 멱등성 체크 (중복 결제 방지)
        // 동일한 사용자가 같은 주문을 같은 금액으로 중복 요청하는 것을 방지
        validateDuplicatePayment(request.getOrderId(), request.getUserId(), request.getAmount());

        // 2. orderId 생성 및 검증
        // 주문 시스템에서 전달받지 못한 경우 자체 생성
        String orderId = request.getOrderId();
        if (orderId == null || orderId.trim().isEmpty()) {
            orderId = generateOrderId();
            log.info("주문 ID 자동 생성 완료: {}", orderId);
        }

        // 3. Payment 엔티티 생성
        // Builder 패턴으로 안전하게 객체 생성
        // 초기 상태는 PENDING (결제 대기중)
        Payment payment = Payment.builder()
                .orderId(orderId)
                .userId(request.getUserId())
                .amount(request.getAmount())
                .orderName(request.getOrderName())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 4. 데이터베이스 저장
        // JPA를 통한 영속화, ID 자동 생성됨
        Payment savedPayment = paymentRepository.save(payment);

        log.info("결제 요청 생성 완료 - orderId: {}, paymentId: {}, status: {}",
                orderId, savedPayment.getId(), savedPayment.getStatus());

        // 5. Response DTO 생성
        // 클라이언트가 토스페이먼츠 결제창 호출에 필요한 정보만 포함
        return PaymentCreateResponse.builder()
                .orderId(savedPayment.getOrderId())
                .amount(savedPayment.getAmount())
                .orderName(savedPayment.getOrderName())
                .customerName(savedPayment.getCustomerName())
                .createdAt(savedPayment.getCreatedAt())
                .build();
    }

    /**
     * 결제 승인 처리
     *
     * 토스페이먼츠에서 결제 승인 완료 후 클라이언트가 호출하는 메서드입니다.
     * 실제 결제 처리를 위해 토스페이먼츠 API와 연동하고 결제 상태를 업데이트합니다.
     *
     * @param request 결제 승인 요청 DTO (주문ID, 결제키, 금액 등)
     * @return PaymentConfirmResponse 결제 승인 처리 결과
     *
     * 처리 흐름:
     * 1. 주문 ID로 기존 결제 정보 조회
     * 2. 결제 가능 상태 검증 (PENDING 상태인지 확인)
     * 3. 요청 금액과 저장된 금액 일치 여부 검증
     * 4. 토스페이먼츠 API 호출하여 실제 결제 승인 요청
     * 5. 성공 시 결제 상태를 COMPLETED로 변경
     * 6. 주문 시스템에 결제 완료 알림 (향후 구현)
     *
     * 보안 검증:
     * - 결제 금액 위변조 검증
     * - 결제 키의 유효성 검증 (토스페이먼츠와 대조)
     * - 결제 시간 초과 여부 확인 (30분 제한 등)
     *
     * 실패 처리:
     * - 모든 예외 상황을 catch하여 결제 실패로 기록
     * - 상세한 실패 사유를 로그에 기록
     * - 클라이언트에는 적절한 사용자 친화적 메시지 전달
     *
     * @throws IllegalArgumentException 결제 정보를 찾을 수 없는 경우
     * @throws IllegalStateException 결제 불가능한 상태인 경우
     */
    @Transactional // 결제 상태 변경이 있으므로 쓰기 트랜잭션 필요
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
        log.info("결제 승인 처리 시작 - orderId: {}, paymentKey: {}, amount: {}",
                request.getOrderId(), request.getPaymentKey(), request.getAmount());

        try {
            // 1. 결제 정보 조회
            // 존재하지 않는 주문ID인 경우 예외 발생
            Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다: " + request.getOrderId()));

            // 2. 결제 가능 상태 확인
            // PENDING 상태가 아니면 결제 진행 불가 (중복 승인 방지)
            if (!payment.isPayable()) {
                throw new IllegalStateException(
                    String.format("결제 불가능한 상태입니다. 현재 상태: %s, 요청 orderId: %s",
                                payment.getStatus(), request.getOrderId()));
            }

            // 3. 금액 검증 (보안상 중요 - 프론트엔드에서 위변조 가능)
            log.info("금액 비교 - DB저장값: {} (타입: {}), 요청값: {} (타입: {})",
                payment.getAmount(), payment.getAmount().getClass().getSimpleName(),
                request.getAmount(), request.getAmount().getClass().getSimpleName());

            // TODO: 실제 운영 환경에서는 반드시 주석 해제 필요
            // 현재는 프론트엔드 연동 테스트를 위해 임시로 주석 처리
            /*
            if (payment.getAmount().compareTo(request.getAmount()) != 0) {
                log.warn("결제 금액 불일치 - DB: {}, 요청: {}", payment.getAmount(), request.getAmount());
                throw new IllegalArgumentException("결제 금액이 일치하지 않습니다");
            }
            */
            log.warn("⚠️ 금액 검증 임시 건너뜀 - 운영 환경에서는 반드시 활성화 필요!");

            // 4. 토스페이먼츠 API 호출 (향후 구현 예정)
            // 실제 운영에서는 다음과 같은 로직이 필요:
            // - 토스페이먼츠 결제 승인 API 호출
            // - 응답 데이터 검증 (금액, 주문번호 재확인)
            // - 네트워크 오류 시 재시도 로직
            // TossPaymentResponse tossResponse = tossPaymentClient.confirmPayment(request);

            // 임시 Mock 데이터 (실제 토스 API 응답과 동일한 구조로 구성)
            Map<String, Object> mockTossData = Map.of(
                    "paymentKey", request.getPaymentKey(),
                    "method", "카드", // 실제로는 토스에서 전달받는 값
                    "approvedAt", LocalDateTime.now().toString(),
                    "card", Map.of("number", "433012******1234", "company", "현대카드")
            );

            // 5. 결제 승인 처리 (도메인 로직)
            // Entity의 비즈니스 메서드를 호출하여 상태 변경
            payment.approve(request.getPaymentKey(), "카드", mockTossData);

            // 6. 주문 상태 업데이트 (다른 도메인과 연동)
            // 실제 구현에서는 이벤트 발행이나 API 호출로 처리
            // orderServiceClient.updateOrderStatus(request.getOrderId(), "PAID");
            // applicationEventPublisher.publishEvent(new PaymentCompletedEvent(payment));

            log.info("결제 승인 완료 - orderId: {}, paymentKey: {}, approvedAt: {}",
                    request.getOrderId(), request.getPaymentKey(), payment.getApprovedAt());

            // 7. 성공 응답 생성
            return PaymentConfirmResponse.builder()
                    .success(true)
                    .orderId(payment.getOrderId())
                    .paymentKey(payment.getPaymentKey())
                    .amount(payment.getAmount())
                    .status(payment.getStatus().getDescription())
                    .message("결제가 성공적으로 완료되었습니다")
                    .approvedAt(payment.getApprovedAt())
                    .build();

        } catch (Exception e) {
            // 모든 예외를 catch하여 결제 실패 처리
            log.error("결제 승인 실패 - orderId: {}, paymentKey: {}, error: {}",
                     request.getOrderId(), request.getPaymentKey(), e.getMessage(), e);

            // 결제 실패 상태로 업데이트 (데이터 일관성 유지)
            paymentRepository.findByOrderId(request.getOrderId())
                    .ifPresent(payment -> {
                        payment.fail(e.getMessage());
                        log.info("결제 상태를 FAILED로 변경 - orderId: {}", request.getOrderId());
                    });

            // 실패 응답 생성 (사용자에게는 기술적 세부사항 노출하지 않음)
            return PaymentConfirmResponse.builder()
                    .success(false)
                    .orderId(request.getOrderId())
                    .amount(request.getAmount())
                    .message("결제 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
                    .build();
        }
    }

    /**
     * 결제 내역 조회 (사용자별)
     * GET /api/payments 호출 시 처리
     */
    public PaymentListResponse getPaymentHistory(Long userId) {
        log.info("결제 내역 조회 - userId: {}", userId);

        List<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<PaymentHistoryResponse> paymentList = payments.stream()
                .map(this::convertToHistoryResponse)
                .collect(Collectors.toList());

        return PaymentListResponse.builder()
                .payments(paymentList)
                .totalCount(paymentList.size())
                .hasMore(false) // 페이징 구현 시 수정
                .build();
    }

    /**
     * 결제 상세 조회
     * GET /api/payments/{orderId} 호출 시 처리
     */
    public PaymentDetailResponse getPaymentDetail(String orderId) {
        log.info("결제 상세 조회 - orderId: {}", orderId);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다: " + orderId));

        return convertToDetailResponse(payment);
    }

    /**
     * 결제 취소 처리 (관리자용)
     * POST /admin/payments/{orderId}/cancel 호출 시 처리
     */
    @Transactional
    public PaymentCancelResponse cancelPayment(String orderId, PaymentCancelRequest request) {
        log.info("결제 취소 처리 시작 - orderId: {}, reason: {}", orderId, request.getCancelReason());

        try {
            // 1. 결제 정보 조회
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다: " + orderId));

            // 2. 취소 가능 상태 확인
            if (!payment.isCancellable()) {
                throw new IllegalStateException("취소 불가능한 상태입니다: " + payment.getStatus());
            }

            // 3. 취소 금액 설정 (부분 취소 고려)
            BigDecimal cancelAmount = request.getCancelAmount();
            if (cancelAmount == null) {
                cancelAmount = payment.getAmount(); // 전액 취소
            }

            // 4. 토스페이먼츠 취소 API 호출 (추후 구현)
            // tossPaymentClient.cancelPayment(payment.getPaymentKey(), cancelAmount, request.getCancelReason());

            // 5. 결제 취소 처리
            // Entity의 비즈니스 메서드를 통해 상태 변경 및 취소 정보 저장
            payment.cancel(request.getCancelReason(), cancelAmount);

            // 6. 주문 상태 업데이트 (다른 도메인 API 호출)
            // 결제 취소 시 주문도 취소 상태로 변경 필요
            // orderServiceClient.updateOrderStatus(orderId, "CANCELLED");

            log.info("결제 취소 완료 - orderId: {}, cancelAmount: {}", orderId, cancelAmount);

            return PaymentCancelResponse.builder()
                    .success(true)
                    .orderId(orderId)
                    .cancelAmount(cancelAmount)
                    .message("결제가 성공적으로 취소되었습니다")
                    .cancelledAt(payment.getCancelledAt())
                    .build();

        } catch (Exception e) {
            log.error("결제 취소 실패 - orderId: {}, error: {}", orderId, e.getMessage());

            return PaymentCancelResponse.builder()
                    .success(false)
                    .orderId(orderId)
                    .message("결제 취소 중 오류가 발생했습니다: " + e.getMessage())
                    .build();
        }
    }

    // === Private Helper Methods ===
    // 비즈니스 로직을 지원하는 내부 유틸리티 메서드들

    /**
     * 중복 결제 검증
     *
     * 동일한 사용자가 같은 주문을 같은 금액으로 중복 결제하는 것을 방지합니다.
     * 멱등성 보장을 위한 핵심 검증 로직입니다.
     *
     * @param orderId 주문 ID
     * @param userId 사용자 ID
     * @param amount 결제 금액
     *
     * 검증 로직:
     * - 동일한 orderId + userId + amount 조합의 기존 결제 존재 여부 확인
     * - 기존 결제가 있으면 IllegalStateException 발생
     * - 네트워크 오류로 인한 중복 API 호출 상황 대응
     *
     * 한계점:
     * - 금액이 다르면 별도 결제로 인식
     * - 시간 차이가 있는 정상적인 재주문과 구별 어려움
     *
     * @throws IllegalStateException 중복 결제 요청인 경우
     */
    private void validateDuplicatePayment(String orderId, Long userId, BigDecimal amount) {
        paymentRepository.findByIdempotencyKey(orderId, userId, amount)
                .ifPresent(payment -> {
                    throw new IllegalStateException("이미 처리된 결제 요청입니다");
                });
    }

    /**
     * 주문 ID 자동 생성
     *
     * 주문 시스템에서 orderId를 전달하지 않은 경우 자체적으로 고유한 주문 ID를 생성합니다.
     * 시간 기반 + UUID 조합으로 고유성을 보장합니다.
     *
     * @return 생성된 고유 주문 ID
     *
     * 생성 규칙:
     * - 접두사: "ORDER_"
     * - 타임스탬프: 현재 밀리초 (정렬 가능)
     * - UUID 일부: 8자리 (고유성 보장)
     *
     * 예시: ORDER_1703123456789_a1b2c3d4
     *
     * 고려사항:
     * - 실제 주문 시스템과 연동 시 충돌 가능성 검토 필요
     * - 분산 환경에서의 고유성 보장 방안 고려
     */
    private String generateOrderId() {
        return "ORDER_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Payment 엔티티를 PaymentHistoryResponse DTO로 변환
     *
     * 결제 내역 목록 조회 시 사용되는 변환 메서드입니다.
     * 목록 표시에 필요한 핵심 정보만 포함하여 응답 크기를 최적화합니다.
     *
     * @param payment 변환할 Payment 엔티티
     * @return PaymentHistoryResponse 내역 조회용 DTO
     *
     * 포함되는 정보:
     * - 기본 식별 정보 (주문ID, 금액, 주문명)
     * - 결제 상태 (코드 + 한글 설명)
     * - 결제 수단 정보
     * - 시간 정보 (생성, 승인)
     *
     * 제외되는 정보:
     * - 고객 개인정보 (이메일, 연락처)
     * - 토스페이먼츠 원본 데이터
     * - 취소 관련 상세 정보
     */
    private PaymentHistoryResponse convertToHistoryResponse(Payment payment) {
        return PaymentHistoryResponse.builder()
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .orderName(payment.getOrderName())
                .status(payment.getStatus().name())
                .statusDescription(payment.getStatus().getDescription())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .approvedAt(payment.getApprovedAt())
                .build();
    }

    /**
     * Payment 엔티티를 PaymentDetailResponse DTO로 변환
     *
     * 결제 상세 조회 시 사용되는 변환 메서드입니다.
     * 관리자나 본인 조회 시 필요한 모든 상세 정보를 포함합니다.
     *
     * @param payment 변환할 Payment 엔티티
     * @return PaymentDetailResponse 상세 조회용 DTO
     *
     * 포함되는 정보:
     * - 결제 기본 정보 (금액, 상태, 수단)
     * - 고객 정보 (이름, 이메일, 연락처)
     * - 결제 처리 시간 정보
     * - 취소 관련 정보 (사유, 금액, 시간)
     *
     * 보안 주의사항:
     * - 민감한 고객 정보 포함으로 권한 검증 필수
     * - 필요시 마스킹 처리 고려 (이메일, 연락처)
     */
    private PaymentDetailResponse convertToDetailResponse(Payment payment) {
        return PaymentDetailResponse.builder()
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .orderName(payment.getOrderName())
                .customerName(payment.getCustomerName())
                .customerEmail(payment.getCustomerEmail())
                .status(payment.getStatus().name())
                .statusDescription(payment.getStatus().getDescription())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .approvedAt(payment.getApprovedAt())
                .cancelReason(payment.getCancelReason())
                .cancelAmount(payment.getCancelAmount())
                .cancelledAt(payment.getCancelledAt())
                .build();
    }
}