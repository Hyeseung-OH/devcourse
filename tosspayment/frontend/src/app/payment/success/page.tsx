// 결제 성공 페이지
'use client'
import { useSearchParams } from 'next/navigation'
import { useEffect, useState } from 'react'
import PaymentResult from '@/components/payment/PaymentResult'

interface PaymentResultData {
  success: boolean
  orderId: string
  paymentKey?: string
  amount: number
  message: string
}

export default function PaymentSuccessPage() {
  const searchParams = useSearchParams()
  const [result, setResult] = useState<PaymentResultData | null>(null)
  const [loading, setLoading] = useState(true)
  
  // 토스페이먼츠에서 리다이렉트로 전달하는 파라미터들
  const paymentKey = searchParams.get('paymentKey')
  const orderId = searchParams.get('orderId')
  const amount = searchParams.get('amount')

  useEffect(() => {
    const processPaymentConfirm = async () => {
      console.log('결제 성공 페이지 파라미터:', { paymentKey, orderId, amount })
      
      if (!paymentKey || !orderId || !amount) {
        setResult({
          success: false,
          orderId: orderId || '알 수 없음',
          amount: Number(amount) || 0,
          message: '결제 정보가 올바르지 않습니다.'
        })
        setLoading(false)
        return
      }

      try {
        console.log('백엔드 결제 승인 요청 시작')
        
        // 백엔드 confirm API 호출
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8070'}/api/payments/confirm`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            paymentKey,
            orderId,
            amount: Number(amount)
          }),
        })

        const confirmResult = await response.json()
        console.log('백엔드 승인 응답:', confirmResult)

        if (confirmResult.success) {
          setResult({
            success: true,
            orderId,
            paymentKey,
            amount: Number(amount),
            message: '결제가 완료되었습니다.'
          })
        } else {
          throw new Error(confirmResult.message || '결제 승인 실패')
        }
        
      } catch (error) {
        console.error('결제 승인 처리 실패:', error)
        setResult({
          success: false,
          orderId,
          amount: Number(amount),
          message: `결제 승인 실패: ${error instanceof Error ? error.message : '알 수 없는 오류'}`
        })
      } finally {
        setLoading(false)
      }
    }

    processPaymentConfirm()
  }, [paymentKey, orderId, amount])

  if (loading) {
    return (
      <div className="max-w-md mx-auto mt-20 text-center">
        <div className="bg-blue-50 p-6 rounded-lg">
          <h1 className="text-xl font-bold text-blue-800 mb-4">결제 처리 중...</h1>
          <p className="text-gray-600">결제 승인을 확인하고 있습니다.</p>
          <div className="mt-4">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto"></div>
          </div>
        </div>
      </div>
    )
  }

  return <PaymentResult result={result} />
}