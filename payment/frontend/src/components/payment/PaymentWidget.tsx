// 토스 위젯 컴포넌트
'use client'
import { useState } from 'react'
import { requestPayment, type PaymentRequestData } from '@/lib/toss-payments'

interface PaymentWidgetProps {
  orderInfo: {
    orderId: string
    amount: number
    orderName: string
    customerName: string
    customerEmail: string
    customerMobilePhone?: string
  }
}

export default function PaymentWidget({ orderInfo }: PaymentWidgetProps) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handlePayment = async () => {
    try {
      setLoading(true)
      setError(null)

      const paymentData: PaymentRequestData = {
        amount: {
          currency: "KRW",
          value: orderInfo.amount,
        },
        orderId: orderInfo.orderId,
        orderName: orderInfo.orderName,
        customerEmail: orderInfo.customerEmail,
        customerName: orderInfo.customerName,
        customerMobilePhone: orderInfo.customerMobilePhone,
      }

      await requestPayment(paymentData)
    } catch (err) {
      setError('결제 요청 실패')
      console.error('결제 실패:', err)
    } finally {
      setLoading(false)
    }
  }

  if (error) {
    return (
      <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
        <p className="text-red-600">{error}</p>
        <button 
          onClick={() => setError(null)}
          className="mt-2 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
        >
          다시 시도
        </button>
      </div>
    )
  }

  return (
    <div className="space-y-4">
      <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
        <h3 className="font-semibold text-blue-800">결제 정보</h3>
        <p>상품명: {orderInfo.orderName}</p>
        <p>금액: {orderInfo.amount.toLocaleString()}원</p>
        <p>주문자: {orderInfo.customerName}</p>
      </div>
      
      <button
        onClick={handlePayment}
        disabled={loading}
        className="w-full p-4 bg-blue-500 text-white rounded-lg hover:bg-blue-600 disabled:bg-gray-400 text-lg font-semibold"
      >
        {loading ? '결제 처리 중...' : '결제하기'}
      </button>
    </div>
  )
}