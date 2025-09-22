// 홈페이지 (결제 페이지로 이동하는 버튼)
import Link from 'next/link'

export default function Home() {
  return (
    <div className="max-w-md mx-auto mt-20 text-center">
      <h1 className="text-2xl font-bold mb-8">결제 시스템 테스트</h1>
      <Link 
        href="/payment"
        className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-600"
      >
        결제하기
      </Link>
    </div>
  )
}