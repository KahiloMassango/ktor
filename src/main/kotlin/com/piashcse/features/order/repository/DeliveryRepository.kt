package com.piashcse.features.order.repository

import com.piashcse.features.order.models.delivery.request.DeliveryStatusUpdate
import com.piashcse.features.order.models.delivery.response.DeliveryResponse

interface DeliveryRepository {
    suspend fun addDelivery(orderId: Int) : DeliveryResponse
    suspend fun getAllDelivers(): List<DeliveryResponse>
    suspend fun getDeliveryById(deliveryId: Int): DeliveryResponse
    suspend fun getDeliveriesByStoreId(storeId: Int): List<DeliveryResponse>
    suspend fun deleteDeliveryById(deliveryId: Int): Int
    suspend fun updateDeliveryStatus(deliveryId: Int, deliveryStatus: DeliveryStatusUpdate): DeliveryResponse
}