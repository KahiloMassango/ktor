package com.piashcse.features.order.models.delivery.response

import com.piashcse.features.order.entities.OrderItemResponse

data class DeliveryResponse(
    val deliveryId: Int,
    val store: DeliveryStore,
    var order: DeliveryOrder,
    var status: String,
)

data class DeliveryStore(
    val storeName: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double
)

data class DeliveryOrder(
    val orderId: Int,
    val customerName: String,
    val customerPhoneNumber: String,
    val deliveryLatitude: Double?,
    val deliveryLongitude: Double?,
    val products: List<OrderItemResponse>
)

