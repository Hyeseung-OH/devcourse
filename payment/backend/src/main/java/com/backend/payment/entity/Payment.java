package com.backend.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 토스페이먼츠에서 발급하는 결제 고유키
     * 결제 승인 후에 생성됨
     */
    @Column(name = "payment_key", length = 100, unique = true)
    private String paymentKey;

    // 주문 ID (다른 도메인에서 전달받음)
    @Column(name = "order_id", length = 50, nullable = false)
    private String orderId;

    // 사용자 ID (다른 도메인에서 전달받음)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 결제 금액 (단위: 원)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // 주문명 (상품명 등)
    @Column(name = "order_name", length = 100, nullable = false)
    private String orderName;

    // 고객명
    @Column(name = "customer_name", length = 50, nullable = false)
    private String customerName;

    // 고객 이메일
    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    // 고객 전화번호
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    // 결제 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    // 결제 수단 (카드, 가상계좌, 간편결제 등)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    // 결제 요청 생성 시간
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // 결제 승인 시간
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // 결제 취소 시간
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    // 취소 사유
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    // 취소 금액 (부분 취소 고려)
    @Column(name = "cancel_amount")
    private BigDecimal cancelAmount;

    /**
     * 토스페이먼츠 API 응답 데이터 (JSON 형태로 저장)
     * 향후 필요한 필드 추가 시 유연하게 대응 가능
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "toss_payment_data", columnDefinition = "json")
    private Map<String, Object> tossPaymentData;

    // === 비즈니스 메서드 ===

    // 결제 승인 처리
    public void approve(String paymentKey, String paymentMethod, Map<String, Object> tossData) {
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.COMPLETED;
        this.approvedAt = LocalDateTime.now();
        this.tossPaymentData = tossData;
    }

    // 결제 실패 처리
    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.cancelReason = reason;
    }

    // 결제 취소 처리
    public void cancel(String cancelReason, BigDecimal cancelAmount) {
        this.status = PaymentStatus.CANCELLED;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
        this.cancelledAt = LocalDateTime.now();
    }

    // 결제 가능 상태인지 확인
    public boolean isPayable() {
        return this.status == PaymentStatus.PENDING;
    }

    // 취소 가능 상태인지 확인
    public boolean isCancellable() {
        return this.status == PaymentStatus.COMPLETED;
    }

    // 멱등키 생성 (중복 결제 방지용)
    public String generateIdempotencyKey() {
        return String.format("%s_%s_%s", orderId, userId, amount);
    }

    // === JPA 생명주기 콜백 ===

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}