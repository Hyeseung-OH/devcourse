package com.backend.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제 관련 에러 코드 정의
 * 에러 상황을 명확하게 분류하기 위한 enum
 */
@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode {

    // 결제 요청 관련
    DUPLICATE_PAYMENT("DUPLICATE_PAYMENT", "이미 처리된 결제입니다"),
    INVALID_AMOUNT("INVALID_AMOUNT", "결제 금액이 올바르지 않습니다"),
    INVALID_ORDER_ID("INVALID_ORDER_ID", "주문번호가 올바르지 않습니다"),

    // 결제 승인 관련
    PAYMENT_NOT_FOUND("PAYMENT_NOT_FOUND", "결제 정보를 찾을 수 없습니다"),
    PAYMENT_ALREADY_CONFIRMED("PAYMENT_ALREADY_CONFIRMED", "이미 승인된 결제입니다"),
    PAYMENT_CANCELLED("PAYMENT_CANCELLED", "취소된 결제입니다"),
    AMOUNT_MISMATCH("AMOUNT_MISMATCH", "결제 금액이 일치하지 않습니다"),

    // 외부 API 관련
    TOSS_API_ERROR("TOSS_API_ERROR", "토스페이먼츠 API 오류가 발생했습니다"),
    EXTERNAL_API_TIMEOUT("EXTERNAL_API_TIMEOUT", "외부 API 요청 시간이 초과되었습니다"),

    // 데이터 관련
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자 정보를 찾을 수 없습니다"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "주문 정보를 찾을 수 없습니다"),

    // 권한 관련
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다"),
    FORBIDDEN("FORBIDDEN", "접근 권한이 없습니다"),

    // 서버 오류
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다"),
    DATABASE_ERROR("DATABASE_ERROR", "데이터베이스 오류가 발생했습니다"),

    // 일반적인 요청 오류
    INVALID_REQUEST("INVALID_REQUEST", "잘못된 요청입니다"),
    MISSING_PARAMETER("MISSING_PARAMETER", "필수 파라미터가 누락되었습니다");

    private final String code;
    private final String message;
}