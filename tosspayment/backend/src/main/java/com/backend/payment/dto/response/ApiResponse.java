package com.backend.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공통 API 응답 래퍼 클래스
 * 모든 API 응답을 통일된 형태로 제공
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 성공 여부
     */
    private boolean success;

    /**
     * 실제 데이터 (성공 시에만 제공)
     */
    private T data;

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 에러 코드 (실패 시에만 제공)
     */
    private String errorCode;

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("성공적으로 처리되었습니다")
                .errorCode(null)
                .build();
    }

    /**
     * 성공 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .errorCode(null)
                .build();
    }

    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    /**
     * 실패 응답 생성 (기본 에러 코드)
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .errorCode("INTERNAL_SERVER_ERROR")
                .build();
    }
}