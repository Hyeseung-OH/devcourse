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
     * 실제 응답 데이터
     * 성공(success=true) 시에만 값이 존재합니다.
     * 실패 시에는 null 또는 제외됩니다.
     * 
     * 제네릭 타입 T로 다양한 응답 데이터 타입을 지원:
     * - PaymentCreateResponse
     * - PaymentListResponse
     * - PaymentDetailResponse 등
     */
    private T data;

    /**
     * 사용자에게 표시될 메시지
     * 성공/실패 상관없이 항상 포함되는 사용자 친화적인 메시지입니다.
     * 
     * 성공 시: "결제가 성공적으로 완료되었습니다"
     * 실패 시: "결제 처리 중 오류가 발생했습니다"
     * 
     * 특징:
     * - 기술적 세부사항 제외
     * - 사용자가 이해하기 쉬운 한국어 메시지
     * - 프론트엔드에서 직접 사용자에게 표시 가능
     */
    private String message;

    /**
     * 에러 코드 (실패 시에만 제공)
     * PaymentErrorCode enum의 code 값이 설정됩니다.
     * 
     * 예시:
     * - "DUPLICATE_PAYMENT"
     * - "PAYMENT_NOT_FOUND"
     * - "AMOUNT_MISMATCH"
     * 
     * 활용:
     * - 프론트엔드에서 에러별 분기 처리
     * - 로깅 및 모니터링 시스템에서 에러 분류
     * - API 문서화에서 에러 케이스 설명
     */
    private String errorCode;

    // === 정적 팩토리 메서드 (Static Factory Methods) ===
    
    /**
     * 성공 응답 생성 (기본 메시지)
     * 
     * 성공적으로 처리된 경우의 응답을 생성합니다.
     * 기본 성공 메시지를 사용합니다.
     * 
     * @param <T> 응답 데이터 타입
     * @param data 실제 응답 데이터
     * @return 성공 응답 객체
     * 
     * 사용 예시:
     * return ApiResponse.success(paymentResponse);
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
     * 
     * 성공 시 특정 메시지를 포함하고 싶을 때 사용합니다.
     * 비즈니스 상황에 맞는 구체적인 메시지 제공이 가능합니다.
     * 
     * @param <T> 응답 데이터 타입
     * @param data 실제 응답 데이터
     * @param message 커스텀 성공 메시지
     * @return 성공 응답 객체
     * 
     * 사용 예시:
     * return ApiResponse.success(paymentResponse, "결제가 성공적으로 완료되었습니다");
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
     * 실패 응답 생성 (에러 코드 포함)
     * 
     * 오류 발생 시 에러 메시지와 에러 코드를 포함한 응답을 생성합니다.
     * 가장 일반적으로 사용되는 실패 응답 생성 메서드입니다.
     * 
     * @param <T> 응답 데이터 타입 (실패 시 사용되지 않음)
     * @param message 사용자 친화적 에러 메시지
     * @param errorCode 시스템 에러 코드 (PaymentErrorCode.getCode())
     * @return 실패 응답 객체
     * 
     * 사용 예시:
     * return ApiResponse.error("결제 정보를 찾을 수 없습니다", "PAYMENT_NOT_FOUND");
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
     * 
     * 예상하지 못한 시스템 오류 등 구체적인 에러 코드를 지정하기 어려운 경우
     * 기본 내부 서버 오류 코드를 사용하여 실패 응답을 생성합니다.
     * 
     * @param <T> 응답 데이터 타입 (실패 시 사용되지 않음)
     * @param message 사용자 친화적 에러 메시지
     * @return 실패 응답 객체 (에러 코드: INTERNAL_SERVER_ERROR)
     * 
     * 사용 예시:
     * return ApiResponse.error("시스템 오류가 발생했습니다");
     * 
     * 주의: 가급적 구체적인 에러 코드를 사용하는 error(message, errorCode) 사용 권장
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .errorCode("INTERNAL_SERVER_ERROR")
                .build();
    }

    // === 편의성 메서드 ===

    /**
     * 성공 상태 확인 편의 메서드
     * 
     * @return 성공 여부 (success 필드와 동일)
     * 
     * 사용 예시:
     * if (response.isSuccess()) { ... }
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * 실패 상태 확인 편의 메서드
     * 
     * @return 실패 여부 (!success)
     * 
     * 사용 예시:
     * if (response.isFailed()) { ... }
     */
    public boolean isFailed() {
        return !this.success;
    }

    /**
     * 에러 코드 존재 여부 확인
     * 
     * @return 에러 코드 존재 여부
     * 
     * 활용: 에러 코드별 분기 처리 전 null 체크
     */
    public boolean hasErrorCode() {
        return this.errorCode != null && !this.errorCode.trim().isEmpty();
    }
}