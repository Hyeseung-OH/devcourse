// 결제 결과 컴포넌트
interface PaymentResultProps {
    result: {
      success: boolean
      orderId: string
      amount: number
      message: string
    } | null
  }
  
  export default function PaymentResult({ result }: PaymentResultProps) {
    if (!result) return null
  
    return (
      <div className="max-w-md mx-auto mt-20 text-center">
        {result.success ? (
          <div className="bg-green-50 p-6 rounded-lg">
            <h1 className="text-2xl font-bold text-green-800 mb-4">결제 완료!</h1>
            <p className="text-gray-600 mb-2">주문번호: {result.orderId}</p>
            <p className="text-gray-600 mb-4">결제금액: {result.amount.toLocaleString()}원</p>
            <button 
              onClick={() => window.location.href = '/'}
              className="bg-green-500 text-white px-6 py-2 rounded-lg"
            >
              홈으로 돌아가기
            </button>
          </div>
        ) : (
          <div className="bg-red-50 p-6 rounded-lg">
            <h1 className="text-2xl font-bold text-red-800 mb-4">결제 실패</h1>
            <p className="text-gray-600 mb-4">{result.message}</p>
            <button 
              onClick={() => window.location.href = '/payment'}
              className="bg-red-500 text-white px-6 py-2 rounded-lg"
            >
              다시 시도
            </button>
          </div>
        )}
      </div>
    )
  }