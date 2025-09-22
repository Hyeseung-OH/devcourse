// 결제 관련 타입 정의
export interface PaymentRequest {
    orderId: string
    amount: number
    orderName: string
    customerName: string
    customerEmail: string
  }
  
  export interface PaymentConfirm {
    paymentKey: string
    orderId: string
    amount: number
  }
  
  export interface PaymentResult {
    success: boolean
    orderId: string
    paymentKey?: string
    amount: number
    message: string
  }