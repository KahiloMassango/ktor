package com.piashcse.features.store.models.response

import com.piashcse.features.order.entities.OrderItemResponse

data class StoreOrderResponse(
    val orderId: Int,
    val customerName: String,
    val customerPhoneNumber: String,
    val total: Double,
    val deliveryMethod: String,
    val products: List<OrderItemResponse>
)


