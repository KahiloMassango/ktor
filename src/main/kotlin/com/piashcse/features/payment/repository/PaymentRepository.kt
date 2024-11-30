package com.piashcse.features.payment.repository

import com.piashcse.features.payment.models.request.AddPaymentBody
import com.piashcse.features.payment.models.request.PaymentStatusUpdate
import com.piashcse.features.payment.models.response.PaymentResponse

interface PaymentRepository {
    suspend fun addPayment(paymentBody: AddPaymentBody): PaymentResponse
    suspend fun findAll(): List<PaymentResponse>
    suspend fun findPaymentBy(paymentId: Int): PaymentResponse

    suspend fun updatePaymentStatus(paymentId: Int, paymentStatus: PaymentStatusUpdate): PaymentResponse
}