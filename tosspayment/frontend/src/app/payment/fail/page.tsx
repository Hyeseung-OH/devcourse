'use client'
import { useSearchParams } from 'next/navigation'
import PaymentResult from '@/components/payment/PaymentResult'

export default function PaymentFailPage() {
  const searchParams = useSearchParams()
  
  const orderId = searchParams.get('orderId') || '알 수 없음'
  const amount = searchParams.get('amount') || '0'
  const code = searchParams.get('code')
  const message = searchParams.get('message')

  const result = {
    success: false,
    orderId,
    amount: Number(amount),
    message: message || '결제가 취소되었습니다.'
  }

  return (
    <div className="max-w-md mx-auto mt-20">
      <div className="bg-red-50 p-6 rounded-lg text-center">
        <h1 className="text-2xl font-bold text-red-800 mb-4">결제 실패</h1>
        <div className="space-y-2 text-sm text-gray-600">
          <p><strong>주문번호:</strong> {orderId}</p>
          <p><strong>금액:</strong> {Number(amount).toLocaleString()}원</p>
          {code && <p><strong>오류 코드:</strong> {code}</p>}
          <p className="text-red-600 mt-4">{result.message}</p>
        </div>
        
        <div className="mt-6 space-x-4">
          <button 
            onClick={() => window.location.href = '/payment'}
            className="bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600"
          >
            다시 결제하기
          </button>
          <button 
            onClick={() => window.location.href = '/'}
            className="bg-gray-500 text-white px-6 py-2 rounded-lg hover:bg-gray-600"
          >
            홈으로
          </button>
        </div>
      </div>
    </div>
  )
}