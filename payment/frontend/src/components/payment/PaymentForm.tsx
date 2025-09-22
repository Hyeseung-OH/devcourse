// 결제 폼 컴포넌트
'use client'
import { useState } from 'react'

interface OrderInfo {
  orderId: string
  amount: number
  orderName: string
  customerName: string
  customerEmail: string
  customerMobilePhone: string
}

export default function PaymentForm() {
  const [orderInfo, setOrderInfo] = useState<OrderInfo>({
    orderId: '',
    amount: 15000,
    orderName: '테스트 상품',
    customerName: '',
    customerEmail: '',
    customerMobilePhone: ''
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    
    try {
      // orderId 미리 생성
      const generatedOrderId = 'ORDER_' + Date.now()
      
      const requestData = {
        orderId: generatedOrderId,
        userId: 1, // 실제로는 context나 props에서 가져옴
        amount: orderInfo.amount,
        orderName: orderInfo.orderName,
        customerName: orderInfo.customerName,
        customerEmail: orderInfo.customerEmail
      }

      console.log('결제 요청 데이터:', requestData)
      
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8070'}/api/payments/request`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`결제 요청 생성 실패: ${response.status} - ${errorText}`)
      }

      const result = await response.json()
      console.log('결제 요청 응답:', result)
      
      // 성공 시 생성한 orderId로 orderInfo 업데이트
      if (result.success && result.data) {
        setOrderInfo(prev => ({ 
          ...prev, 
          orderId: generatedOrderId 
        }))
      } else {
        throw new Error(result.message || '결제 요청 생성에 실패했습니다')
      }
      
    } catch (error) {
      console.error('주문 생성 실패:', error)
      setError(error instanceof Error ? error.message : '결제 요청 생성 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (field: keyof OrderInfo, value: string) => {
    setOrderInfo(prev => ({ 
      ...prev, 
      [field]: value 
    }))
  }

  const handlePayment = async () => {
    setLoading(true)
    
    try {
      // Mock paymentKey 생성
      const mockPaymentKey = 'mock_payment_key_' + Date.now()
      
      console.log('결제 승인 요청:', {
        paymentKey: mockPaymentKey,
        orderId: orderInfo.orderId,
        amount: orderInfo.amount
      })
      
      // 백엔드 confirm API 호출
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8070'}/api/payments/confirm`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          paymentKey: mockPaymentKey,
          orderId: orderInfo.orderId,
          amount: orderInfo.amount
        })
      })
      
      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`결제 승인 실패: ${response.status} - ${errorText}`)
      }
      
      const result = await response.json()
      console.log('결제 승인 응답:', result)
      
      if (result.success && result.data) {
        alert('결제 성공! 결제 키: ' + result.data.paymentKey)
        window.location.href = `/payment/success?paymentKey=${result.data.paymentKey}&orderId=${result.data.orderId}&amount=${result.data.amount}`
      } else {
        throw new Error(result.message || '결제 승인에 실패했습니다.')
      }
      
    } catch (error) {
      console.error('결제 실패:', error)
      setError(error instanceof Error ? error.message : '결제 처리 중 오류가 발생했습니다.')
      alert('결제 실패: ' + (error instanceof Error ? error.message : '알 수 없는 오류'))
    } finally {
      setLoading(false)
    }
  }

  const handleReset = () => {
    setOrderInfo(prev => ({ 
      ...prev, 
      orderId: '' 
    }))
    setError(null)
  }

  return (
    <div>
      {!orderInfo.orderId ? (
        // 고객 정보 입력 폼
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-lg">
              <p className="text-red-600 text-sm">{error}</p>
            </div>
          )}
          
          <div>
            <label htmlFor="customerName" className="block text-sm font-medium mb-1">
              이름 *
            </label>
            <input
              id="customerName"
              type="text"
              placeholder="이름을 입력하세요"
              value={orderInfo.customerName}
              onChange={(e) => handleInputChange('customerName', e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
              disabled={loading}
            />
          </div>
          
          <div>
            <label htmlFor="customerEmail" className="block text-sm font-medium mb-1">
              이메일 *
            </label>
            <input
              id="customerEmail"
              type="email"
              placeholder="이메일을 입력하세요"
              value={orderInfo.customerEmail}
              onChange={(e) => handleInputChange('customerEmail', e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
              disabled={loading}
            />
          </div>
          
          <div>
            <label htmlFor="customerMobilePhone" className="block text-sm font-medium mb-1">
              전화번호 *
            </label>
            <input
              id="customerMobilePhone"
              type="tel"
              placeholder="전화번호를 입력하세요 (예: 01012341234)"
              value={orderInfo.customerMobilePhone}
              onChange={(e) => handleInputChange('customerMobilePhone', e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
              disabled={loading}
            />
          </div>

          <div className="bg-blue-50 p-4 rounded-lg">
            <h4 className="font-medium text-blue-800 mb-2">주문 정보</h4>
            <p className="text-blue-700">상품: {orderInfo.orderName}</p>
            <p className="text-blue-700 font-semibold">금액: {orderInfo.amount.toLocaleString()}원</p>
          </div>
          
          <button 
            type="submit"
            className="w-full bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
            disabled={loading || !orderInfo.customerName || !orderInfo.customerEmail || !orderInfo.customerMobilePhone}
          >
            {loading ? '처리 중...' : '결제 정보 생성'}
          </button>
        </form>
      ) : (
        // 결제 화면
        <div className="space-y-4">
          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-lg">
              <p className="text-red-600 text-sm">{error}</p>
            </div>
          )}

          <div className="bg-gray-50 p-4 rounded-lg">
            <h3 className="text-lg font-semibold mb-3">결제 정보 확인</h3>
            <div className="space-y-1 text-sm">
              <p><span className="font-medium">주문번호:</span> {orderInfo.orderId}</p>
              <p><span className="font-medium">상품명:</span> {orderInfo.orderName}</p>
              <p><span className="font-medium">주문자:</span> {orderInfo.customerName}</p>
              <p><span className="font-medium">이메일:</span> {orderInfo.customerEmail}</p>
              <p><span className="font-medium">전화번호:</span> {orderInfo.customerMobilePhone}</p>
              <p className="text-lg font-bold text-blue-600 mt-2">
                <span className="font-medium text-black">결제금액:</span> {orderInfo.amount.toLocaleString()}원
              </p>
            </div>
          </div>
          
          <div className="space-y-3">
            <h4 className="font-medium">결제 수단 선택</h4>
            <div className="space-y-2">
              <label className="flex items-center p-3 border rounded-lg hover:bg-gray-50 cursor-pointer">
                <input type="radio" name="paymentMethod" value="card" defaultChecked className="mr-3" />
                <div>
                  <div className="font-medium">신용카드</div>
                  <div className="text-sm text-gray-500">모든 신용카드 이용 가능</div>
                </div>
              </label>
              <label className="flex items-center p-3 border rounded-lg hover:bg-gray-50 cursor-pointer">
                <input type="radio" name="paymentMethod" value="account" className="mr-3" />
                <div>
                  <div className="font-medium">계좌이체</div>
                  <div className="text-sm text-gray-500">실시간 계좌이체</div>
                </div>
              </label>
            </div>
          </div>
          
          <button 
            onClick={handlePayment}
            className="w-full bg-green-500 text-white p-4 rounded-lg hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors font-semibold"
            disabled={loading}
          >
            {loading ? '결제 처리 중...' : `${orderInfo.amount.toLocaleString()}원 결제하기`}
          </button>
          
          <button 
            onClick={handleReset}
            className="w-full bg-gray-300 text-gray-700 p-2 rounded-lg hover:bg-gray-400 transition-colors"
            disabled={loading}
          >
            다시 입력하기
          </button>
        </div>
      )}
    </div>
  )
}