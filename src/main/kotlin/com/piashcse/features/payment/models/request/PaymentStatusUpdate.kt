package com.piashcse.features.payment.models.request

import org.valiktor.functions.isIn
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class PaymentStatusUpdate(val status: String) {
    fun validation() {
        validate(this) {
            validate(PaymentStatusUpdate::status).isNotNull().isNotEmpty().isIn(
                PaymentStatus.PROCESSING.name.lowercase(),
                PaymentStatus.PAID.name.lowercase(),
                PaymentStatus.CANCELED.name.lowercase()
            )
        }
    }
}
