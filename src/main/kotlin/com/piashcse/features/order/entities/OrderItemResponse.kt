package com.piashcse.features.order.entities

import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object OrderItemTable : BaseIntIdTable("order_items") {
    val orderId = reference("order_id", OrderTable.id)
    val productName = varchar("product_name", 255)
    val color = varchar("color", 30).nullable()
    val size = varchar("size", 30).nullable()
    val quantity = integer("quantity")
    val price = double("price")
}

class OrderItemEntity(id: EntityID<Int>) : BaseIntEntity(id, OrderItemTable) {
    companion object : BaseIntEntityClass<OrderItemEntity>(OrderItemTable)

    var orderId by OrderItemTable.orderId
    var productName by OrderItemTable.productName
    var quantity by OrderItemTable.quantity
    var color by OrderItemTable.color
    var size by OrderItemTable.size
    var price by OrderItemTable.price

    fun response() = OrderItemResponse(
        productName,
        color,
        size,
        quantity,
        price
    )
}

data class OrderItemResponse(
    val productName: String,
    val color: String?,
    val size: String?,
    val quantity: Int,
    val price: Double
)