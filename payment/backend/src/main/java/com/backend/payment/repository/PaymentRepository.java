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

/**
 * 결제 정보에 대한 데이터 접근 계층 인터페이스
 * Spring Data JPA를 활용하여 결제 엔티티의 CRUD 및 복잡한 쿼리를 처리합니다.
 * 
 * 주요 기능:
 * - 기본 CRUD 연산 (JpaRepository 상속)
 * - 비즈니스 로직에 특화된 조회 메서드들
 * - 중복 결제 방지를 위한 멱등키 기반 조회
 * - 통계 및 관리 기능을 위한 집계 쿼리
 * 
 * 성능 최적화 고려사항:
 * - 자주 사용되는 조회 조건에 대해 데이터베이스 인덱스 설정 필요
 * - 대용량 데이터 조회 시 페이징 처리 권장
 * - 복잡한 집계 쿼리는 읽기 전용 복제본 DB 활용 고려
 * 
 * @author Backend Team
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * 주문 ID로 결제 정보 단건 조회
     * 
     * 주문 시스템과의 연동에서 가장 빈번하게 사용되는 조회 메서드입니다.
     * 하나의 주문에는 일반적으로 하나의 결제만 연결되므로 Optional을 반환합니다.
     * 
     * @param orderId 조회할 주문 ID
     * @return 해당 주문의 결제 정보 (없으면 Optional.empty())
     * 
     * 사용 시나리오:
     * - 주문 상세 페이지에서 결제 정보 표시
     * - 주문 상태 업데이트 시 결제 상태 확인
     * - 배송 처리 전 결제 완료 여부 검증
     * 
     * 성능 고려사항:
     * - order_id 컬럼에 인덱스 설정 필수
     * - 가장 많이 호출되는 메서드이므로 쿼리 성능 모니터링 필요
     */
    Optional<Payment> findByOrderId(String orderId);

    /**
     * 토스페이먼츠 결제 키로 결제 정보 단건 조회
     * 
     * 토스페이먼츠 웹훅 처리 시 사용되는 핵심 메서드입니다.
     * paymentKey는 토스페이먼츠에서 결제 승인 후 발급하는 고유 식별자입니다.
     * 
     * @param paymentKey 토스페이먼츠 결제 키
     * @return 해당 결제 키의 결제 정보 (없으면 Optional.empty())
     * 
     * 사용 시나리오:
     * - 토스페이먼츠 웹훅 수신 시 결제 정보 업데이트
     * - 결제 상태 조회 API에서 토스 연동 정보 확인
     * - 결제 취소 요청 시 토스페이먼츠 API 호출용
     * 
     * 보안 고려사항:
     * - paymentKey는 외부에서 추측하기 어려운 값이므로 보안상 안전
     * - 웹훅 요청의 진위성 검증과 함께 사용 권장
     */
    Optional<Payment> findByPaymentKey(String paymentKey);

    /**
     * 사용자별 결제 내역 조회 (최신순 정렬)
     * 
     * 고객의 결제 이력을 시간순으로 조회하는 메서드입니다.
     * 마이페이지나 고객 상담 시 결제 내역 확인용으로 사용됩니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 모든 결제 내역 (최신순)
     * 
     * 사용 시나리오:
     * - 마이페이지 결제 내역 표시
     * - 고객 상담 시 결제 이력 조회
     * - 사용자별 결제 패턴 분석
     * 
     * 성능 최적화 방안:
     * - 대용량 사용자의 경우 페이징 처리 필요
     * - user_id + created_at 복합 인덱스 설정 권장
     * - 필요시 Pageable 파라미터를 받는 오버로드 메서드 추가 고려
     */
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 사용자별 특정 상태의 결제 내역 조회
     * 
     * 특정 결제 상태의 내역만 필터링하여 조회하는 메서드입니다.
     * 예: 완료된 결제만, 취소된 결제만 등
     * 
     * @param userId 조회할 사용자 ID  
     * @param status 필터링할 결제 상태
     * @return 해당 사용자의 특정 상태 결제 내역 (최신순)
     * 
     * 사용 시나리오:
     * - 완료된 결제만 표시하는 매출 관리
     * - 실패한 결제 분석 및 재결제 유도
     * - 취소된 결제 내역을 통한 환불 관리
     * 
     * 활용 예시:
     * - findByUserIdAndStatusOrderByCreatedAtDesc(userId, PaymentStatus.COMPLETED)
     * - findByUserIdAndStatusOrderByCreatedAtDesc(userId, PaymentStatus.FAILED)
     */
    List<Payment> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PaymentStatus status);

    /**
     * 특정 기간 내 결제 내역 조회 (관리자용)
     * 
     * 관리자 대시보드나 매출 분석을 위한 기간별 결제 데이터 조회 메서드입니다.
     * 복잡한 날짜 범위 조건을 JPQL로 구현하여 가독성과 유지보수성을 높였습니다.
     * 
     * @param startDate 조회 시작 일시 (포함)
     * @param endDate 조회 종료 일시 (포함)  
     * @return 해당 기간의 모든 결제 내역 (최신순)
     * 
     * 사용 시나리오:
     * - 일별/월별/연도별 매출 분석
     * - 특정 이벤트 기간의 결제 현황 분석
     * - 정산 업무를 위한 기간별 결제 데이터 추출
     * 
     * 성능 주의사항:
     * - 긴 기간 조회 시 많은 데이터가 반환될 수 있음
     * - created_at 컬럼에 인덱스 설정 필수
     * - 대용량 데이터의 경우 페이징이나 스트리밍 방식 고려
     */
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * 상태별 결제 내역 조회 (관리자용)
     * 
     * 특정 결제 상태의 모든 결제를 조회하는 관리자용 메서드입니다.
     * 상태별 결제 현황 파악이나 문제 상황 모니터링에 활용됩니다.
     * 
     * @param status 조회할 결제 상태
     * @return 해당 상태의 모든 결제 내역 (최신순)
     * 
     * 사용 시나리오:
     * - 실패한 결제 현황 분석 및 원인 파악
     * - 대기 상태로 남은 결제 건들의 후속 처리
     * - 취소된 결제들의 환불 처리 현황 확인
     * 
     * 관리 활용:
     * - findByStatusOrderByCreatedAtDesc(PaymentStatus.FAILED) : 실패 결제 분석
     * - findByStatusOrderByCreatedAtDesc(PaymentStatus.PENDING) : 미처리 결제 확인
     */
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);

    /**
     * 멱등키 기반 중복 결제 확인
     * 
     * 동일한 주문에 대한 중복 결제 요청을 방지하기 위한 핵심 메서드입니다.
     * orderId, userId, amount 조합으로 고유성을 보장합니다.
     * 
     * @param orderId 주문 ID
     * @param userId 사용자 ID  
     * @param amount 결제 금액
     * @return 동일 조건의 기존 결제 (없으면 Optional.empty())
     * 
     * 중복 결제 방지 로직:
     * 1. 결제 요청 접수 시 이 메서드로 기존 결제 존재 여부 확인
     * 2. 기존 결제가 있고 COMPLETED 상태면 중복 결제 차단
     * 3. 기존 결제가 PENDING 상태면 해당 결제로 진행
     * 4. 기존 결제가 FAILED면 새로운 결제 요청 허용
     * 
     * 비즈니스 규칙:
     * - 동일 사용자가 같은 주문을 같은 금액으로 중복 결제하는 것 방지
     * - 네트워크 오류로 인한 중복 API 호출 상황 대응
     * - 사용자의 실수로 인한 중복 결제 클릭 방지
     * 
     * 한계점:
     * - 금액이 다르면 별도 결제로 인식 (부분 결제 시나리오에서 주의)
     * - 시간 차이가 있는 정상적인 재주문과 구별 어려움
     */
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId AND p.userId = :userId AND p.amount = :amount")
    Optional<Payment> findByIdempotencyKey(@Param("orderId") String orderId,
                                           @Param("userId") Long userId,
                                           @Param("amount") BigDecimal amount);

    /**
     * 취소 가능한 결제 조회
     * 
     * 결제 취소 요청 시 해당 결제가 실제로 취소 가능한 상태인지 확인하는 메서드입니다.
     * COMPLETED 상태이면서 지정된 paymentKey와 일치하는 결제를 조회합니다.
     * 
     * @param paymentKey 취소하려는 결제의 토스페이먼츠 키
     * @return 취소 가능한 결제 정보 (조건에 맞지 않으면 Optional.empty())
     * 
     * 취소 가능 조건:
     * - 결제 상태가 COMPLETED (승인 완료)
     * - paymentKey가 정확히 일치
     * - 이미 취소되거나 실패한 결제는 제외
     * 
     * 사용 시나리오:
     * - 관리자의 결제 취소 요청 처리
     * - 고객의 결제 취소 요청 검증
     * - 자동 취소 배치 작업에서 대상 결제 확인
     * 
     * 보안 고려사항:
     * - paymentKey 검증으로 무단 취소 요청 차단
     * - 취소 권한이 있는 사용자만 이 메서드 결과를 활용해야 함
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'COMPLETED' AND p.paymentKey = :paymentKey")
    Optional<Payment> findCancellablePayment(@Param("paymentKey") String paymentKey);

    /**
     * 특정 사용자의 총 결제 건수 조회
     * 
     * 사용자의 결제 활동 수준을 파악하기 위한 집계 메서드입니다.
     * 상태에 관계없이 모든 결제 시도를 카운트합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 총 결제 건수
     * 
     * 활용 분야:
     * - 고객 등급 산정 (VIP, 일반 등)
     * - 사용자별 결제 패턴 분석
     * - 마케팅 타겟팅을 위한 고객 분류
     * - 이상 행위 탐지 (비정상적으로 많은 결제 시도)
     * 
     * 성능 최적화:
     * - user_id 인덱스 활용으로 빠른 집계
     * - 필요시 Redis 캐시에 결과 저장 고려
     */
    long countByUserId(Long userId);

    /**
     * 오늘 완료된 결제 건수 조회 (관리자 대시보드용)
     * 
     * 실시간 매출 모니터링을 위한 당일 결제 완료 건수를 조회합니다.
     * 관리자 대시보드의 주요 KPI 지표로 활용됩니다.
     * 
     * @return 오늘 날짜의 결제 완료 건수
     * 
     * 쿼리 특징:
     * - DATE() 함수로 시간 부분을 제거하여 날짜만 비교
     * - CURRENT_DATE로 데이터베이스 서버의 현재 날짜 사용
     * - COMPLETED 상태만 카운트하여 실제 매출 건수 반영
     * 
     * 사용 시나리오:
     * - 관리자 메인 대시보드의 오늘 매출 현황
     * - 실시간 매출 모니터링 및 알림
     * - 일별 매출 트렌드 분석의 기초 데이터
     * 
     * 성능 개선 방안:
     * - created_at, status 복합 인덱스 설정
     * - 캐시를 활용하여 자주 조회되는 값 저장
     * - 배치 작업으로 일별 통계 테이블 별도 관리 고려
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE DATE(p.createdAt) = CURRENT_DATE AND p.status = 'COMPLETED'")
    long countTodayCompletedPayments();
}