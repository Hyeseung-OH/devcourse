// 전체 레이아웃 설정
import './globals.css'
import type { Metadata } from 'next'
import Script from 'next/script'

export const metadata: Metadata = {
  title: '토스 페이먼츠 연동 테스트',
  description: '결제 시스템 테스트',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="ko">
      <body>
        <div className="min-h-screen bg-gray-50">
          <nav className="bg-white shadow-sm p-4">
            <h1 className="text-xl font-bold">결제 테스트</h1>
          </nav>
          <main className="container mx-auto p-4">
            {children}
          </main>
        </div>

        {/* 이벤트 핸들러 제거 */}
        <Script
          src="https://js.tosspayments.com/v1/payment"
          strategy="beforeInteractive"
        />
      </body>
    </html>
  )
}