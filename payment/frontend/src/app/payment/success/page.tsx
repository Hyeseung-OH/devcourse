// 결제 성공 페이지
'use client'
import { useSearchParams } from 'next/navigation'
import PaymentResult from '@/components/payment/PaymentResult'

export default function PaymentSuccessPage() {
  const searchParams = useSearchParams()
  
  const paymentKey = searchParams.get('paymentKey')
  const orderId = searchParams.get('orderId')
  const amount = searchParams.get('amount')

  const result = paymentKey && orderId && amount ? {
    success: true,
    orderId: orderId,
    paymentKey: paymentKey,
    amount: Number(amount),
    message: '결제가 완료되었습니다.'
  } : {
    success: false,
    orderId: orderId || '알 수 없음',
    amount: Number(amount) || 0,
    message: '결제 정보가 올바르지 않습니다.'
  }

  return <PaymentResult result={result} />
}