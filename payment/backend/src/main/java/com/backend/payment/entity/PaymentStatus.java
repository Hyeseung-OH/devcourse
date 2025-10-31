package com.backend.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 결제 상태를 나타내는 Enum
@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    // 결제 대기 - 결제 요청이 생성되었지만 아직 승인되지 않은 상태
    PENDING("결제 대기"),

    // 결제 완료 - 토스페이먼츠에서 승인이 완료된 상태
    COMPLETED("결제 완료"),

    // 결제 실패 - 결제 과정에서 실패한 상태
    FAILED("결제 실패"),

    // 결제 취소 - 관리자나 시스템에 의해 취소된 상태
    CANCELLED("결제 취소");

    private final String description;

    // 결제 진행 가능한 상태들
    public static PaymentStatus[] getProcessableStatuses() {
        return new PaymentStatus[]{PENDING};
    }

    // 취소 가능한 상태들
    public static PaymentStatus[] getCancellableStatuses() {
        return new PaymentStatus[]{COMPLETED};
    }

    /**
     * 결제 취소 상태인지 확인
     * 환불 처리된 결제인지 판단
     * 
     * @return 취소 상태 여부
     * 
     * 활용:
     * - 환불 완료 알림 발송 조건
     * - 취소된 주문에 대한 재주문 허용 여부
     * - 매출 통계에서 취소 건수 집계
     */
    public boolean isCancelled() {
        return this == CANCELLED;
    }

    /**
     * 결제 실패 상태인지 확인
     * 승인 과정에서 실패한 결제인지 판단
     * 
     * @return 실패 상태 여부
     * 
     * 활용:
     * - 실패 사유 분석 및 통계
     * - 고객 안내 메시지 분기 처리
     * - 재결제 유도 로직 실행 조건
     */
    public boolean isFailed() {
        return this == FAILED;
    }

    /**
     * 결제 대기 상태인지 확인
     * 아직 승인되지 않은 결제인지 판단
     * 
     * @return 대기 상태 여부
     * 
     * 활용:
     * - 결제 진행 가능 여부 확인
     * - 대기중인 결제 건수 모니터링
     * - 시간 초과된 대기 건에 대한 정리 작업
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * 최종 상태인지 확인 (더 이상 변경되지 않는 상태)
     * 
     * @return 최종 상태 여부
     * 
     * 최종 상태: COMPLETED, FAILED, CANCELLED
     * 비최종 상태: PENDING (아직 다른 상태로 전환 가능)
     * 
     * 활용:
     * - 상태 변경 로직에서 검증 조건
     * - 배치 작업에서 처리 완료된 건 제외
     * - 알림 발송 대상 필터링
     */
    public boolean isFinalState() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }

    /**
     * 성공적인 결제 상태인지 확인 (실제 매출이 발생한 상태)
     * 
     * @return 매출 발생 상태 여부
     * 
     * 매출 발생 상태: COMPLETED
     * 비매출 상태: PENDING, FAILED, CANCELLED
     * 
     * 활용:
     * - 실제 매출 집계 시 사용
     * - 정산 대상 결제 필터링
     * - 수수료 계산 대상 확인
     */
    public boolean isRevenue() {
        return this == COMPLETED;
    }
}