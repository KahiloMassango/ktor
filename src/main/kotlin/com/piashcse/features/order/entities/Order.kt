package com.piashcse.features.order.entities

import com.piashcse.features.customer.entities.CustomerTable
import com.piashcse.features.order.models.order.request.OrderStatus
import com.piashcse.features.order.models.order.response.OrderResponse
import com.piashcse.features.product.entities.ProductEntity
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import com.piashcse.features.store.entities.StoreTable
import org.jetbrains.exposed.dao.id.EntityID

object OrderTable : BaseIntIdTable("order") {
    val customerId = reference("customer_id", CustomerTable.id)
    val storeId = reference("store_id", StoreTable.id)
    val quantity = integer("quantity") // total number of items
    val subTotal = double("sub_total")
    val total = double("total")
    val deliveryMethod = varchar("delivery_type", 30)
    val deliveryFee = double("delivery_fee")
    val status = varchar("status", 30).default(OrderStatus.PROCESSING.status)
    val addressName = varchar("address_name", 255).default("")
    val deliveryLatitude = double("delivery_latitude").nullable()
    val deliveryLongitude = double("delivery_longitude").nullable()
}

class OrderEntity(id: EntityID<Int>) : BaseIntEntity(id, OrderTable) {
    companion object : BaseIntEntityClass<OrderEntity>(OrderTable)

    var customerId by OrderTable.customerId
    var storeId by OrderTable.storeId
    var quantity by OrderTable.quantity
    var subTotal by OrderTable.subTotal
    var total by OrderTable.total
    var deliveryMethod by OrderTable.deliveryMethod
    var deliveryFee by OrderTable.deliveryFee
    var status by OrderTable.status
    var addressName by OrderTable.addressName
    var deliveryLatitude by OrderTable.deliveryLatitude
    var deliveryLongitude by OrderTable.deliveryLongitude

    private val items by OrderItemEntity referrersOn OrderItemTable

    fun response() = OrderResponse(
        id.value,
        customerId.value,
        storeId.value,
        quantity,
        subTotal,
        total,
        deliveryMethod,
        deliveryFee,
        addressName,
        deliveryLatitude,
        deliveryLongitude,
        items.map { it.response() }
    )
}
