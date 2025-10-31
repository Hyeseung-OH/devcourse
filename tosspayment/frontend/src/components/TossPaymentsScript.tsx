// TossPaymentsScript.tsx
"use client"

import Script from "next/script"

export default function TossPaymentsScript() {
  return (
    <Script
      src="https://js.tosspayments.com/v1/payment-widget"
      strategy="beforeInteractive"
      onLoad={() => {
        console.log("✅ 토스페이먼츠 스크립트 로드 완료")
      }}
      onError={() => {
        console.error("❌ 토스페이먼츠 스크립트 로드 실패")
      }}
    />
  )
}
