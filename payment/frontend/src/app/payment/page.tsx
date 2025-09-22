// 결제 페이지
import PaymentForm from '@/components/payment/PaymentForm'

export default function PaymentPage() {
  return (
    <div className="max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-6">상품 결제</h1>
      
      {/* 상품 정보 표시 */}
      <div className="bg-white p-4 rounded-lg shadow mb-6">
        <h2 className="text-lg font-semibold">테스트 상품</h2>
        <p className="text-gray-600">상품 설명입니다.</p>
        <p className="text-xl font-bold text-blue-600 mt-2">15,000원</p>
      </div>

      {/* 결제 폼 */}
      <PaymentForm />
    </div>
  )
}