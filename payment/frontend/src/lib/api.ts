// 백엔드 API 호출 함수
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8070'

// 수정된 인터페이스
interface PaymentRequestData {
  amount: number
  orderName: string
  customerName: string
  customerEmail: string
  customerMobilePhone: string // 추가
}

// 결제 요청 생성
export async function createPaymentRequest(orderInfo: {
  amount: number
  orderName: string
  customerName: string
  customerEmail: string
}) {
  // orderId와 userId 추가 필요
  const requestData = {
    ...orderInfo,
    orderId: 'ORDER_' + Date.now(), // 고유한 orderId 생성
    userId: 1 // 임시 사용자 ID (실제로는 로그인한 사용자 ID 사용)
  };

  const response = await fetch(`${API_BASE_URL}/api/payments/request`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(requestData),
  })

  if (!response.ok) {
    throw new Error('결제 요청 생성 실패')
  }

  return response.json()
}

// 결제 승인 요청
export async function confirmPayment(confirmData: {
  paymentKey: string
  orderId: string
  amount: number
}) {
  const response = await fetch(`${API_BASE_URL}/api/payments/confirm`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(confirmData),
  })

  if (!response.ok) {
    throw new Error('결제 승인 실패')
  }

  return response.json()
}