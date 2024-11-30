package com.piashcse.features.order.models.order.request

import org.valiktor.functions.isIn
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class OrderStatusUpdateBody(val status: String) {
    fun validation() {
        validate(this) {
            validate(OrderStatusUpdateBody::status).isNotNull().isIn(
                OrderStatus.PROCESSING.name.lowercase(),
                OrderStatus.DELIVERED.name.lowercase(),
                OrderStatus.CANCELED.name.lowercase(),
            )
        }
    }
}
