package com.piashcse.features.payment.models.response


data class PaymentResponse(
    val id: Int,
    val orderId: Int,
    val amount: Double,
    val status: String,
    val paymentMethod: String
)
