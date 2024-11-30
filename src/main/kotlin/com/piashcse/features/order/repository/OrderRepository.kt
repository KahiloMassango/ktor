package com.piashcse.features.order.repository

import com.piashcse.features.order.models.order.request.AddOrderBody
import com.piashcse.features.order.models.order.request.OrderStatusUpdateBody
import com.piashcse.features.order.models.order.response.CustomerOrderResponse
import com.piashcse.features.order.models.order.response.OrderResponse
import com.piashcse.features.store.models.response.StoreOrderResponse

interface OrderRepository {
    suspend fun addOrder(customerId: Int, orderBody: AddOrderBody) : OrderResponse
    suspend fun getOrders() : List<OrderResponse>
    suspend fun getOrderById(orderId: Int): OrderResponse

    suspend fun getOrderByStoreId(storeId: Int, orderId: Int): StoreOrderResponse
    suspend fun getOrdersByStoreId(storeId: Int): List<StoreOrderResponse>

    suspend fun getOrderByCustomerId(customerId: Int, orderId: Int): CustomerOrderResponse
    suspend fun getOrdersByCustomerId(customerId: Int): List<OrderResponse>

    suspend fun updateOrderStatus(storeId: Int, orderId: Int, orderStatus: OrderStatusUpdateBody): OrderResponse
}