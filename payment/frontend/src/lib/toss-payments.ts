// 실제 토스 페이먼츠 SDK 타입에 맞게 수정
import { loadTossPayments, ANONYMOUS } from "@tosspayments/tosspayments-sdk"

const CLIENT_KEY = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY!
const CUSTOMER_KEY = "d-FYnMUaf4LWX2bUhfhCm"

// 토스 페이먼츠 타입 정의
interface TossPaymentsSDK {
  payment: (options: PaymentOptions) => PaymentInstance
}

interface PaymentOptions {
  customerKey: string
}

interface PaymentInstance {
  requestPayment: (params: PaymentRequestParams) => Promise<void>
}

interface PaymentRequestParams {
  method: string
  amount: {
    currency: string
    value: number
  }
  orderId: string
  orderName: string
  successUrl: string
  failUrl: string
  customerEmail?: string
  customerName?: string
  customerMobilePhone?: string
  card?: {
    useEscrow: boolean
    flowMode: string
    useCardPoint: boolean
    useAppCardOnly: boolean
  }
}

let tossPaymentsInstance: TossPaymentsSDK | null = null
let paymentInstance: PaymentInstance | null = null

export async function initializeTossPayments(): Promise<PaymentInstance> {
  if (tossPaymentsInstance && paymentInstance) {
    return paymentInstance
  }

  try {
    // 타입 단언을 사용하여 SDK 로드
    tossPaymentsInstance = await loadTossPayments(CLIENT_KEY) as TossPaymentsSDK
    
    paymentInstance = tossPaymentsInstance.payment({
      customerKey: CUSTOMER_KEY,
    })
    
    return paymentInstance
  } catch (error) {
    console.error('토스 페이먼츠 초기화 실패:', error)
    throw error
  }
}

export interface PaymentRequestData {
  amount: {
    currency: string
    value: number
  }
  orderId: string
  orderName: string
  customerEmail?: string
  customerName?: string
  customerMobilePhone?: string
}

export async function requestPayment(data: PaymentRequestData): Promise<void> {
  const payment = await initializeTossPayments()
  
  return payment.requestPayment({
    method: "CARD",
    amount: data.amount,
    orderId: data.orderId,
    orderName: data.orderName,
    successUrl: `${window.location.origin}/payment/success`,
    failUrl: `${window.location.origin}/payment/fail`,
    customerEmail: data.customerEmail,
    customerName: data.customerName,
    customerMobilePhone: data.customerMobilePhone,
    card: {
      useEscrow: false,
      flowMode: "DEFAULT",
      useCardPoint: false,
      useAppCardOnly: false,
    },
  })
}