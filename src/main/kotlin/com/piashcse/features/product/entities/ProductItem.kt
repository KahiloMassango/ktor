package com.piashcse.features.product.entities

import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ProductItemTable : BaseIntIdTable("product_item") {
    val productId = reference("product_id", ProductTable.id, ReferenceOption.CASCADE)
    val qtyStock = integer("qty_stock")
    val image = varchar("image", 255).nullable()
    val price = double("price")
}

class ProductItemEntity(id: EntityID<Int>) : BaseIntEntity(id, ProductItemTable) {
    companion object : BaseIntEntityClass<ProductItemEntity>(ProductItemTable)

    var productId by ProductItemTable.productId
    var qtyStock by ProductItemTable.qtyStock
    var image by ProductItemTable.image
    var price by ProductItemTable.price

    private var attributes by VariationOptionEntity via ProductConfigurationTable



    fun response() = ProductItemResponse(
        id.value,
        price,
        qtyStock,
        image,
        attributes.find { it.response().variationName == "Size" }?.response()?.value,
        attributes.find { it.response().variationName == "Color" }?.response()?.value
    )

}

data class ProductItemResponse(
    val id: Int,
    val price: Double,
    val stockQuantity: Int,
    val image: String?,
    val size: String?,
    val color: String?
)

