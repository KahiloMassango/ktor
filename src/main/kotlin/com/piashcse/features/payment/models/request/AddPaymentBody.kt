package com.piashcse.features.payment.models.request

import org.valiktor.functions.isIn
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddPaymentBody(
    val orderId: Int,
    val amount: Double,
    val paymentMethod: String
) {
    fun validation() {
        validate(this) {
            validate(AddPaymentBody::orderId).isNotNull()
            validate(AddPaymentBody::amount).isNotNull()
            validate(AddPaymentBody::paymentMethod).isNotNull().isNotEmpty().isIn(
                PaymentStatus.PROCESSING.name.lowercase(),
                PaymentStatus.PAID.name.lowercase(),
                PaymentStatus.CANCELED.name.lowercase()
            )
        }
    }
}
