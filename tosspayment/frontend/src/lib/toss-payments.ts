// 토스페이먼츠 Widget API 올바른 사용법
const CLIENT_KEY = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY!

// 실제 토스페이먼츠 Widget API 타입 정의
declare global {
  interface Window {
    TossPayments: (clientKey: string) => {
      requestPayment: (params: PaymentRequestParams) => Promise<void>
    }
  }
}

interface PaymentRequestParams {
  method: string
  amount: {
    currency: string
    value: number
  }
  orderId: string
  orderName: string
  successUrl: string
  failUrl: string
  customerEmail?: string
  customerName?: string
  customerMobilePhone?: string
  card?: {
    useEscrow?: boolean
    flowMode?: string
    useCardPoint?: boolean
    useAppCardOnly?: boolean
  }
}

export interface PaymentRequestData {
  amount: {
    currency: string
    value: number
  }
  orderId: string
  orderName: string
  customerEmail?: string
  customerName?: string
  customerMobilePhone?: string
}

export async function requestPayment(data: PaymentRequestData): Promise<void> {
  try {
    console.log('=== 토스페이먼츠 결제 시작 ===')
    console.log('CLIENT_KEY 확인:', CLIENT_KEY ? '✅ 존재' : '❌ 없음')
    console.log('결제 데이터:', data)
    
    // 토스페이먼츠 스크립트 로드 대기
    await waitForTossPayments()
    
    // 토스페이먼츠 인스턴스 생성 (Widget API 방식)
    const tossPayments = window.TossPayments(CLIENT_KEY)
    console.log('✅ TossPayments 인스턴스 생성 완료')
    console.log('TossPayments 메서드들:', Object.keys(tossPayments))
    
    // 결제 요청 파라미터
    const paymentParams: PaymentRequestParams = {
      method: "CARD", // 결제 수단
      amount: data.amount,
      orderId: data.orderId,
      orderName: data.orderName,
      successUrl: `${window.location.origin}/payment/success`,
      failUrl: `${window.location.origin}/payment/fail`,
      customerEmail: data.customerEmail,
      customerName: data.customerName,
      customerMobilePhone: data.customerMobilePhone,
      card: {
        useEscrow: false,
        flowMode: "DEFAULT",
        useCardPoint: false,
        useAppCardOnly: false,
      },
    }
    
    console.log('결제 요청 파라미터:', paymentParams)
    
    // 토스페이먼츠 Widget API로 직접 결제 요청 (payment() 메서드 없이)
    await tossPayments.requestPayment(paymentParams)
    console.log('✅ 결제 요청 완료 - 토스페이먼츠 창으로 이동')
    
  } catch (error: unknown) {
    if (error instanceof Error) {
      console.error('❌ 결제 실패:', error.message)
      throw new Error(error.message)
    } else {
      console.error('❌ 알 수 없는 에러:', error)
      throw new Error('알 수 없는 오류가 발생했습니다.')
    }
  }
  
}

// 토스페이먼츠 스크립트 로드 대기 (디버깅 강화)
function waitForTossPayments(): Promise<void> {
  return new Promise((resolve, reject) => {
    console.log('토스페이먼츠 스크립트 로드 상태 확인 중...')
    
    // 이미 로드되어 있으면 바로 resolve
    if (window.TossPayments) {
      console.log('✅ TossPayments 이미 로드됨')
      
      // 실제 구조 디버깅
      try {
        const testInstance = window.TossPayments(CLIENT_KEY)
        console.log('TossPayments 인스턴스:', testInstance)
        console.log('사용 가능한 메서드들:', Object.keys(testInstance))
        console.log('requestPayment 메서드 존재:', typeof testInstance.requestPayment)
      } catch (e) {
        console.error('인스턴스 생성 테스트 실패:', e)
      }
      
      resolve()
      return
    }
    
    // 최대 15초 대기
    let attempts = 0
    const maxAttempts = 75 // 15초 (200ms * 75)
    
    const checkTossPayments = () => {
      attempts++
      console.log(`토스페이먼츠 로드 확인 시도 ${attempts}/${maxAttempts}`)
      
      if (window.TossPayments) {
        console.log(`✅ TossPayments 스크립트 로드 완료 (${attempts * 200}ms 소요)`)
        
        // 로드 완료 후 구조 확인
        try {
          const testInstance = window.TossPayments(CLIENT_KEY)
          console.log('TossPayments 테스트 인스턴스:', testInstance)
          console.log('사용 가능한 메서드들:', Object.keys(testInstance))
        } catch (e) {
          console.error('인스턴스 생성 테스트 실패:', e)
        }
        
        resolve()
        return
      }
      
      if (attempts >= maxAttempts) {
        console.error('❌ TossPayments 스크립트 로드 시간 초과')
        console.error('현재 window 객체에 있는 토스 관련:', Object.keys(window).filter(key => key.toLowerCase().includes('toss')))
        reject(new Error('결제 시스템 로드에 실패했습니다. 페이지를 새로고침 해주세요.'))
        return
      }
      
      // 200ms 후 다시 확인
      setTimeout(checkTossPayments, 200)
    }
    
    checkTossPayments()
  })
}