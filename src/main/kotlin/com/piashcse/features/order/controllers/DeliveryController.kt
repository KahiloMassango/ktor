package com.piashcse.features.order.controllers

import com.piashcse.features.order.entities.DeliveryEntity
import com.piashcse.features.order.entities.DeliveryTable
import com.piashcse.features.order.entities.OrderEntity
import com.piashcse.features.order.entities.OrderTable
import com.piashcse.features.order.models.delivery.request.DeliveryStatusUpdate
import com.piashcse.features.order.models.delivery.response.DeliveryResponse
import com.piashcse.features.order.repository.DeliveryRepository
import com.piashcse.features.store.entities.StoreTable
import com.piashcse.utils.CommonException
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll

class DeliveryController : DeliveryRepository {

    override suspend fun addDelivery(orderId: Int): DeliveryResponse = query {
        val deliveryExists = DeliveryEntity.find { DeliveryTable.orderId eq orderId }.singleOrNull()
        deliveryExists?.let {
            throw CommonException("Delivery for order $orderId already exists")
        }
        val order = OrderEntity.findById(orderId) ?: throw orderId.notFoundException()
        DeliveryEntity.new {
            this.orderId = order.id
        }.response()
    }

    override suspend fun getAllDelivers(): List<DeliveryResponse> = query {
        DeliveryEntity.all().map { it.response() }
    }

    override suspend fun getDeliveryById(deliveryId: Int): DeliveryResponse = query {
        DeliveryEntity.findById(deliveryId)?.response() ?: throw deliveryId.notFoundException("delivery not found")
    }

    override suspend fun getDeliveriesByStoreId(storeId: Int): List<DeliveryResponse> = query {
        val query = DeliveryTable.innerJoin(OrderTable).innerJoin(StoreTable).selectAll()
            .andWhere { StoreTable.id eq storeId }
            //.andWhere { StoreTable.isActive eq true }

        DeliveryEntity.wrapRows(query).map { it.response() }
    }

    override suspend fun deleteDeliveryById(deliveryId: Int): Int = query {
        DeliveryEntity.findById(deliveryId)?.let {
            it.delete()
            deliveryId
        } ?: throw deliveryId.notFoundException()
    }

    override suspend fun updateDeliveryStatus(
        deliveryId: Int,
        deliveryStatus: DeliveryStatusUpdate
    ): DeliveryResponse = query {
        DeliveryEntity.findById(deliveryId)?.let {
            it.status = deliveryStatus.status
            it.response()
        } ?: throw NotFoundException()
    }
}