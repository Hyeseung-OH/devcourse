package com.backend.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 결제 정보를 저장하는 엔티티 클래스
 * 토스페이먼츠 PG 연동을 기준으로 설계되었으며, 결제의 전체 생명주기를 관리합니다.
 * 
 * 주요 기능:
 * - 결제 요청 생성 및 관리
 * - 결제 승인/실패/취소 상태 관리
 * - 토스페이먼츠 API 응답 데이터 저장
 * - 부분 취소 지원
 * 
 * 보안 고려사항:
 * - 민감한 카드 정보는 직접 저장하지 않음 (PG사에서 토큰화 처리)
 * - 결제 상태 변경은 비즈니스 메서드를 통해서만 가능
 * 
 * @author Backend Team
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "payment", indexes = {
    @Index(name = "idx_payment_order_id", columnList = "order_id"),
    @Index(name = "idx_payment_user_id", columnList = "user_id"),
    @Index(name = "idx_payment_status", columnList = "status"),
    @Index(name = "idx_payment_created_at", columnList = "created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자, 외부에서 직접 생성 방지
@AllArgsConstructor
@Builder
public class Payment {

    /**
     * 결제 테이블의 기본키
     * 자동 증가값으로 설정하여 성능 최적화
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 토스페이먼츠에서 발급하는 결제 고유키
     * 결제 승인 완료 후에만 생성되며, 토스페이먼츠 API 호출 시 필수값
     * unique 제약조건으로 중복 방지
     */
    @Column(name = "payment_key", length = 100, unique = true)
    private String paymentKey;

    /**
     * 주문 시스템에서 전달받는 주문 ID
     * 다른 도메인(주문, 상품)과의 연결점 역할
     * 비즈니스적으로 중요한 식별자이므로 NOT NULL 처리
     */
    @Column(name = "order_id", length = 50, nullable = false)
    private String orderId;

    /**
     * 결제를 요청한 사용자의 ID
     * 사용자 시스템과의 연결점 역할
     * 결제 내역 조회 시 필터링 기준으로 사용
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 결제 요청 금액 (단위: 원)
     * BigDecimal 사용으로 정확한 금액 계산 보장
     * 부동소수점 오차 방지를 위해 정수형 대신 BigDecimal 선택
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 0)
    private BigDecimal amount;

    /**
     * 주문명/상품명
     * 사용자에게 표시되는 결제 항목명
     * 토스페이먼츠 결제창에서 사용자에게 노출됨
     */
    @Column(name = "order_name", length = 100, nullable = false)
    private String orderName;

    /**
     * 결제 고객의 실명
     * 신용카드 결제 시 카드 소유자명과 일치해야 함
     * 가상계좌 발급 시 입금자명 확인용으로 사용
     */
    @Column(name = "customer_name", length = 50, nullable = false)
    private String customerName;

    /**
     * 고객 이메일 주소
     * 결제 완료 알림 발송 시 사용
     * 필수값은 아니지만 고객 서비스 향상을 위해 권장
     */
    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    /**
     * 고객 연락처
     * 결제 문제 발생 시 연락용
     * 가상계좌 입금 안내 SMS 발송 시 사용
     */
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    /**
     * 현재 결제 상태
     * ENUM 타입으로 관리하여 잘못된 상태값 입력 방지
     * STRING 타입으로 저장하여 데이터베이스에서 가독성 확보
     * 기본값은 PENDING (결제 대기중)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    /**
     * 실제 사용된 결제 수단
     * 예: CARD, VIRTUAL_ACCOUNT, EASY_PAY 등
     * 토스페이먼츠에서 전달받는 값으로 설정됨
     */
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    /**
     * 결제 요청이 최초 생성된 시간
     * 불변값으로 설정하여 수정 방지 (updatable = false)
     * 결제 처리 시간 분석 및 통계 생성에 활용
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 결제 승인이 완료된 시간
     * 실제 결제가 성공한 시점을 기록
     * 정산 및 매출 분석 시 기준 시간으로 활용
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    /**
     * 결제 취소가 처리된 시간
     * 환불 처리 완료 시점 기록
     * 취소 처리 시간 분석에 활용
     */
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    /**
     * 결제 취소 사유
     * 고객 또는 관리자가 입력한 취소 사유
     * 취소 통계 분석 및 CS 대응에 활용
     */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    /**
     * 취소된 금액
     * 부분 취소를 지원하기 위해 별도 필드로 관리
     * 전액 취소인 경우 amount와 동일한 값
     */
    @Column(name = "cancel_amount", precision = 10, scale = 0)
    private BigDecimal cancelAmount;

    /**
     * 토스페이먼츠 API 원본 응답 데이터
     * JSON 형태로 저장하여 향후 필드 추가 시 유연하게 대응
     * 
     * 저장되는 주요 정보:
     * - 카드 정보 (마스킹된 카드번호, 카드사, 할부개월 등)
     * - 가상계좌 정보 (계좌번호, 은행, 입금기한 등)
     * - 간편결제 정보 (카카오페이, 네이버페이 등)
     * 
     * 보안: 민감한 정보는 토스페이먼츠에서 이미 마스킹 처리됨
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "toss_payment_data", columnDefinition = "json")
    private Map<String, Object> tossPaymentData;


    // === 비즈니스 메서드 ===
    // 도메인 로직을 캡슐화하여 데이터 일관성과 비즈니스 규칙 준수

    /**
     * 결제 승인 처리
     * 토스페이먼츠에서 결제 승인 완료 후 호출되는 메서드
     * 
     * @param paymentKey 토스페이먼츠 결제 고유키
     * @param paymentMethod 실제 사용된 결제 수단
     * @param tossData 토스페이먼츠 API 응답 원본 데이터
     * 
     * 주의사항:
     * - 이미 승인된 결제에 대해서는 중복 처리 방지 필요
     * - 승인 시간은 서버 시간 기준으로 자동 설정
     */
    public void approve(String paymentKey, String paymentMethod, Map<String, Object> tossData) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태가 아닌 결제는 승인할 수 없습니다. 현재 상태: " + this.status);
        }
        
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.COMPLETED;
        this.approvedAt = LocalDateTime.now();
        this.tossPaymentData = tossData;
    }

    /**
     * 결제 실패 처리
     * 결제 과정에서 오류 발생 시 호출되는 메서드
     * 
     * @param reason 결제 실패 사유 (토스페이먼츠 에러 메시지 또는 자체 검증 실패 사유)
     * 
     * 활용 사례:
     * - 카드 한도 초과, 잔액 부족 등의 결제 오류
     * - 중복 결제 시도 등의 비즈니스 규칙 위반
     * - PG사 서버 오류 등의 기술적 문제
     */
    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.cancelReason = reason;
        // 실패 시간도 기록하는 것을 고려해볼 수 있음 (향후 개선 사항)
    }

    /**
     * 결제 취소 처리
     * 승인된 결제에 대해 전액 또는 부분 취소를 처리
     * 
     * @param cancelReason 취소 사유 (고객 요청, 관리자 판단, 시스템 오류 등)
     * @param cancelAmount 취소할 금액 (부분 취소 지원)
     * 
     * 비즈니스 규칙:
     * - 취소 금액은 원결제 금액을 초과할 수 없음
     * - 이미 취소된 결제는 중복 취소 불가
     * - 취소 처리 시간은 서버 시간으로 자동 기록
     */
    public void cancel(String cancelReason, BigDecimal cancelAmount) {
        if (!isCancellable()) {
            throw new IllegalStateException("취소 가능한 상태가 아닙니다. 현재 상태: " + this.status);
        }
        
        if (cancelAmount.compareTo(this.amount) > 0) {
            throw new IllegalArgumentException("취소 금액이 원결제 금액을 초과할 수 없습니다.");
        }
        
        this.status = PaymentStatus.CANCELLED;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
        this.cancelledAt = LocalDateTime.now();
    }

    /**
     * 결제 가능 상태 확인
     * 결제 승인 요청 전 상태 검증용
     * 
     * @return 결제 가능 여부 (true: 결제 가능, false: 결제 불가)
     * 
     * 사용 시나리오:
     * - 결제 요청 API 호출 시 사전 검증
     * - 중복 결제 방지
     * - 결제 가능 상태만 토스페이먼츠로 전달
     */
    public boolean isPayable() {
        return this.status == PaymentStatus.PENDING;
    }

    /**
     * 취소 가능 상태 확인
     * 결제 취소 요청 전 상태 검증용
     * 
     * @return 취소 가능 여부 (true: 취소 가능, false: 취소 불가)
     * 
     * 취소 가능 조건:
     * - 결제 상태가 COMPLETED (승인 완료)
     * - 이미 취소되거나 실패한 결제는 취소 불가
     */
    public boolean isCancellable() {
        return this.status == PaymentStatus.COMPLETED;
    }

    /**
     * 멱등키(Idempotency Key) 생성
     * 중복 결제 방지를 위한 고유 식별자 생성
     * 
     * @return 주문ID_사용자ID_금액 형태의 고유 문자열
     * 
     * 활용 방안:
     * - 동일한 주문에 대한 중복 결제 요청 차단
     * - Redis 또는 Cache를 활용한 중복 요청 검증
     * - PG사 API 호출 시 멱등키로 활용
     * 
     * 주의사항:
     * - 동일한 사용자가 같은 금액의 다른 주문을 할 경우 고려 필요
     * - 보다 정교한 멱등키 생성 로직으로 개선 가능 (timestamp 추가 등)
     */
    public String generateIdempotencyKey() {
        return String.format("%s_%s_%s", orderId, userId, amount);
    }

    // === JPA 생명주기 콜백 ===
    // 엔티티 영속성 관리를 위한 생명주기 이벤트 처리

    /**
     * 엔티티 영속화 직전 호출되는 콜백 메서드
     * 생성 시간이 설정되지 않은 경우 자동으로 현재 시간 설정
     * 
     * 사용 목적:
     * - Builder 패턴 사용 시 createdAt 누락 방지
     * - 테스트 코드에서 시간 설정 없이 엔티티 생성 시 자동 처리
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}