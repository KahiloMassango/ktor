package com.piashcse.features.product.entities

import com.piashcse.features.product.models.product.response.ProductResponse
import com.piashcse.features.product.models.product.response.ProductWithVariationResponse
import com.piashcse.features.product.models.product.response.ProductStoreResponse
import com.piashcse.features.store.entities.StoreEntity
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import com.piashcse.features.store.entities.StoreTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ProductTable : BaseIntIdTable("product") {
    val title = varchar("title", length = 255)
    val description = text("description")
    val storeId = reference("store_id", StoreTable.id, onDelete = ReferenceOption.CASCADE)
    val categoryId = reference("category_id", CategoryTable.id, onDelete = ReferenceOption.CASCADE)
    val subCategoryId = reference("sub_category_id", SubCategoryTable.id)
    val isAvailable = bool("is_available").default(false)
    val image = text("image")
}

class ProductEntity(id: EntityID<Int>) : BaseIntEntity(id, ProductTable) {
    companion object : BaseIntEntityClass<ProductEntity>(ProductTable)

    var storeId by ProductTable.storeId
    var categoryId by ProductTable.categoryId
    var subCategoryId by ProductTable.subCategoryId
    var title by ProductTable.title
    var description by ProductTable.description
    var isAvailable by ProductTable.isAvailable
    var image by ProductTable.image

    val store by StoreEntity referencedOn ProductTable.storeId
    val items by ProductItemEntity referrersOn ProductItemTable orderBy ProductItemTable.price
    val reviews by ReviewEntity referrersOn ReviewTable
    val subCategory by SubCategoryEntity referencedOn ProductTable.subCategoryId

    fun response() = ProductResponse(
        id.value,
        title,
        description,
        ProductStoreResponse(store.id.value, store.name),
        image,
       // items.map { it.price }[0]
    )

    fun responseWithVariation() = ProductWithVariationResponse(
        id.value,
        title,
        description,
        ProductStoreResponse(store.id.value, store.name),
        image,
        subCategory.name,
        items.map { it.response() }.filter { it.stockQuantity > 0 },
        reviews.map { it.response() }
    )
}
