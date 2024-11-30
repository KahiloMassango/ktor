package com.piashcse.features.order.models.order.response

import com.piashcse.features.order.entities.OrderItemResponse

data class OrderResponse(
    val orderId: Int,
    val customerId: Int,
    val storeId: Int,
    val quantity: Int,
    val subTotal: Double,
    val total: Double,
    val deliveryMethod: String,
    val deliveryFee: Double,
    val addressName: String,
    val deliveryLatitude: Double?,
    val deliveryLongitude: Double?,
    val products: List<OrderItemResponse>
)

data class CustomerOrderResponse(
    val orderId: Int,
    val quantity: Int,
    val total: Double,
    val status: String,
    val deliveryMethod: String,
    val deliveryAddressName: String,
    val date: String,
    val products: List<CustomerOrderItemResponse>
)

data class CustomerOrderItemResponse(
    val productName: String,
    val storeName: String,
    val color: String?,
    val size: String?,
    val quantity: Int,
    val price: Double
)
