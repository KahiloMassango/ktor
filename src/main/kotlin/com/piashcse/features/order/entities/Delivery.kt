package com.piashcse.features.order.entities

import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.order.models.delivery.request.DeliveryStatus
import com.piashcse.features.order.models.delivery.response.DeliveryOrder
import com.piashcse.features.order.models.delivery.response.DeliveryResponse
import com.piashcse.features.order.models.delivery.response.DeliveryStore
import com.piashcse.features.store.entities.StoreEntity
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object DeliveryTable : BaseIntIdTable("shipping") {
    val orderId = reference("order_id", OrderTable.id)
    val status = varchar("status", 20).default(DeliveryStatus.PROCESSING.status)


}

class DeliveryEntity(id: EntityID<Int>) : BaseIntEntity(id, DeliveryTable) {
    companion object : BaseIntEntityClass<DeliveryEntity>(DeliveryTable)

    var orderId by DeliveryTable.orderId
    var status by DeliveryTable.status

    private val order by OrderEntity referencedOn DeliveryTable.orderId
    // Dynamically get the store associated with the order
    val store: StoreEntity
        get() = StoreEntity.findById(order.storeId.value)!!
    // Dynamically get the customer associated with the order
    val customer: CustomerEntity
        get() = CustomerEntity.findById(order.customerId.value)!!



    fun response() =
        DeliveryResponse(
            id.value,
            DeliveryStore(store.name, store.phoneNumber, store.latitude, store.longitude),
            DeliveryOrder(
                orderId.value,
                customer.username,
                customer.phoneNumber,
                order.deliveryLatitude,
                order.deliveryLongitude,
                order.response().products
            ),
            status

        )
}

