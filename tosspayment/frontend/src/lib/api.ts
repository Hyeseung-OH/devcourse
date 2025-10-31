// 백엔드 API 호출 함수
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8070'

// 백엔드 API와 일치하는 인터페이스
interface PaymentRequestData {
  orderId: string
  userId: number
  amount: number
  orderName: string
  customerName: string
  customerEmail: string
  customerPhone: string // 백엔드 필드명에 맞춤
}

// 결제 요청 생성
export async function createPaymentRequest(orderInfo: {
  orderId: string
  userId: number
  amount: number
  orderName: string
  customerName: string
  customerEmail: string
  customerPhone: string
}) {
  console.log('API 요청 데이터:', orderInfo)
  
  const response = await fetch(`${API_BASE_URL}/api/payments/request`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(orderInfo),
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`결제 요청 생성 실패: ${response.status} - ${errorText}`)
  }

  return response.json()
}

// 결제 승인 요청
export async function confirmPayment(confirmData: {
  paymentKey: string
  orderId: string
  amount: number
}) {
  console.log('결제 승인 요청:', confirmData)
  
  const response = await fetch(`${API_BASE_URL}/api/payments/confirm`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(confirmData),
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`결제 승인 실패: ${response.status} - ${errorText}`)
  }

  return response.json()
}

// API 응답 타입 정의
export interface ApiResponse<T> {
  success: boolean
  data?: T
  message: string
  errorCode?: string
}

export interface PaymentRequestResponse {
  orderId: string
  amount: number
  orderName: string
  customerName: string
  createdAt: string
}