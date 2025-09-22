package com.backend.payment.repository;

import com.backend.payment.entity.Payment;
import com.backend.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * orderId로 결제 정보 조회
     * 가장 많이 사용되는 조회 방법
     */
    Optional<Payment> findByOrderId(String orderId);

    /**
     * paymentKey로 결제 정보 조회
     * 토스페이먼츠 웹훅 처리 시 사용
     */
    Optional<Payment> findByPaymentKey(String paymentKey);

    /**
     * 사용자별 결제 내역 조회 (최신순)
     * 마이페이지에서 사용
     */
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 사용자별 특정 상태의 결제 내역 조회
     */
    List<Payment> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PaymentStatus status);

    /**
     * 특정 기간 내 결제 내역 조회 (관리자용)
     */
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * 상태별 결제 내역 조회 (관리자용)
     */
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);

    /**
     * 멱등키로 중복 결제 확인
     * orderId + userId + amount 조합으로 중복 결제 방지
     */
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId AND p.userId = :userId AND p.amount = :amount")
    Optional<Payment> findByIdempotencyKey(@Param("orderId") String orderId,
                                           @Param("userId") Long userId,
                                           @Param("amount") BigDecimal amount);

    /**
     * 결제 완료된 건 중에서 취소 가능한 결제 조회
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'COMPLETED' AND p.paymentKey = :paymentKey")
    Optional<Payment> findCancellablePayment(@Param("paymentKey") String paymentKey);

    /**
     * 특정 사용자의 결제 건수 조회
     */
    long countByUserId(Long userId);

    /**
     * 오늘 결제 건수 조회 (관리자 대시보드용)
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE DATE(p.createdAt) = CURRENT_DATE AND p.status = 'COMPLETED'")
    long countTodayCompletedPayments();
}