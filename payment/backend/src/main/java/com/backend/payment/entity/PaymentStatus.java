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

    // 완료된 상태인지 확인
    public boolean isCompleted() {
        return this == COMPLETED;
    }

    // 취소된 상태인지 확인
    public boolean isCancelled() {
        return this == CANCELLED;
    }

    // 실패한 상태인지 확인
    public boolean isFailed() {
        return this == FAILED;
    }

    // 대기 상태인지 확인
    public boolean isPending() {
        return this == PENDING;
    }
}