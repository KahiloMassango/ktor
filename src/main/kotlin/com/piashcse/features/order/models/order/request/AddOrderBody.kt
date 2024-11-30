package com.piashcse.features.order.models.order.request

import org.valiktor.functions.*
import org.valiktor.validate

data class AddOrderBody(
    val storeId: Int,
    val subTotal: Double,
    val total: Double,
    val deliveryMethod: String,
    val deliveryFee: Double,
    val deliveryLatitude: Double?,
    val deliveryLongitude: Double?,
    val orderItems: List<OrderProductItem>
) {
    fun validation() {
        validate(this) {
            validate(AddOrderBody::subTotal).isNotNull().isGreaterThan(0.0)
            validate(AddOrderBody::total).isNotNull().isGreaterThan(0.0)
            validate(AddOrderBody::deliveryFee).isNotNull().isGreaterThanOrEqualTo(0.0)
            validate(AddOrderBody::orderItems).isNotNull().isNotEmpty()
            validate(AddOrderBody::deliveryMethod).isIn(DeliveryMethod.DELIVERY.method, DeliveryMethod.PICKUP.method)
        }
    }
}

