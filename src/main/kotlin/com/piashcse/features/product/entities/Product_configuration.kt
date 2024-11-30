package com.piashcse.features.product.entities

import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ProductConfigurationTable : BaseIntIdTable("product_configuration") {
    val productItemId = reference("product_item_id", ProductItemTable.id, ReferenceOption.CASCADE)
    val variationOptionId = reference("variation_option_id", VariationOptionTable.id)
}

class ProductConfigurationEntity(id: EntityID<Int>): BaseIntEntity(id, ProductConfigurationTable) {
    companion object : BaseIntEntityClass<ProductConfigurationEntity>(ProductConfigurationTable)

    var productItemId by ProductConfigurationTable.productItemId
    var variationOptionId by ProductConfigurationTable.variationOptionId

    fun response() = ProductConfiguration(
        id.value,
        productItemId.value,
        variationOptionId.value
    )
}


data class ProductConfiguration(
    val id: Int,
    val productItemId: Int,
    val variationOptionId: Int
)