package com.piashcse.features.order.models.order.request

import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class OrderProductItem(
    val productItemId: Int,
    val quantity: Int
) {
    fun validation() {
        validate(this) {
            validate(OrderProductItem::productItemId).isNotNull()
            validate(OrderProductItem::quantity).isNotNull().isGreaterThan(0)
        }
    }
}
