package com.piashcse.features.payment.controllers

import com.piashcse.features.order.entities.OrderEntity
import com.piashcse.features.order.entities.OrderTable
import com.piashcse.features.payment.entities.PaymentEntity
import com.piashcse.features.payment.entities.PaymentTable
import com.piashcse.features.payment.models.request.AddPaymentBody
import com.piashcse.features.payment.models.request.PaymentStatusUpdate
import com.piashcse.features.payment.models.response.PaymentResponse
import com.piashcse.features.payment.repository.PaymentRepository
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class PaymentController: PaymentRepository {

    override suspend fun addPayment(paymentBody: AddPaymentBody): PaymentResponse = query {
        val order = OrderEntity.find { OrderTable.id eq paymentBody.orderId }.toList().firstOrNull()
            ?: throw paymentBody.orderId.notFoundException()
        PaymentEntity.new {
            orderId = order.id
            amount = paymentBody.amount
            paymentMethod = paymentBody.paymentMethod
        }.response()
    }

    override suspend fun findAll(): List<PaymentResponse> = query {
        PaymentEntity.all().map { it.response() }
    }

    override suspend fun findPaymentBy(paymentId: Int): PaymentResponse = query {
        PaymentEntity.find { PaymentTable.id eq paymentId }
            .toList()
            .singleOrNull()?.response() ?: throw paymentId.notFoundException()
    }

    override suspend fun updatePaymentStatus(paymentId: Int, paymentStatus: PaymentStatusUpdate): PaymentResponse = query {
        PaymentEntity.find { PaymentTable.id eq paymentId }.toList().singleOrNull()
            ?.let {
                it.status = paymentStatus.status
                it.response()
            } ?: throw paymentId.notFoundException()
    }
}