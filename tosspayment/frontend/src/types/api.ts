// API 응답 타입 정의
export interface ApiResponse<T> {
    success: boolean
    data?: T
    message: string
  }
  
  export interface PaymentRequestResponse {
    orderId: string
    amount: number
    createdAt: string
  }