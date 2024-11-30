package com.piashcse.features.order.models.delivery.request

import org.valiktor.functions.isIn
import org.valiktor.functions.isNotNull
import org.valiktor.validate


data class DeliveryStatusUpdate(
    val status: String
) {
    fun validation() {
        validate(this) {
            validate(DeliveryStatusUpdate::status).isNotNull().isIn(
                DeliveryStatus.PROCESSING.name.lowercase(),
                DeliveryStatus.DELIVERED.name.lowercase()
            )
        }
    }
}