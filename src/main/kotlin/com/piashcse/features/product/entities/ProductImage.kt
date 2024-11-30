package com.piashcse.features.product.entities

import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object ProductImageTable : BaseIntIdTable("product_image") {
    val productId = reference("product_id", ProductTable.id)
    val imageUrl = text("image_url") // multiple image will be saved comma seperated string
}

class ProductImageEntity(id: EntityID<Int>) : BaseIntEntity(id, ProductImageTable) {
    companion object : BaseIntEntityClass<ProductImageEntity>(ProductImageTable)

    var productId by ProductImageTable.productId
    var imageUrl by ProductImageTable.imageUrl

    fun response() = ProductImage(id.value, productId.value, imageUrl)
}

data class ProductImage(
    val id: Int,
    val productId: Int,
    val imageUrl: String,
)