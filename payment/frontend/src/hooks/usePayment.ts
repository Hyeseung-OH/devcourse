// 결제 관련 커스텀 훅
'use client'
import { useState, useCallback } from 'react'
import { createPaymentRequest, confirmPayment } from '@/lib/api'
import type { PaymentRequest, PaymentConfirm } from '@/types/payment'

export function usePayment() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const requestPayment = useCallback(async (data: PaymentRequest) => {
    setLoading(true)
    setError(null)
    
    try {
      const result = await createPaymentRequest(data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : '결제 요청 실패')
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  const confirmPaymentRequest = useCallback(async (data: PaymentConfirm) => {
    setLoading(true)
    setError(null)
    
    try {
      const result = await confirmPayment(data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : '결제 승인 실패')
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  return {
    loading,
    error,
    requestPayment,
    confirmPaymentRequest
  }
}