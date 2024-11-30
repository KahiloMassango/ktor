package com.piashcse.features.product.entities

import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object VariationOptionTable: BaseIntIdTable("variation_option") {
    val value = varchar("value", 60)
    val variationId = reference("variation", VariationTable.id)
}

class VariationOptionEntity(id: EntityID<Int>) : BaseIntEntity(id, VariationOptionTable) {
    companion object : BaseIntEntityClass<VariationOptionEntity>(VariationOptionTable)

    var value by VariationOptionTable.value
    var variationId by VariationOptionTable.variationId

    private val variation by VariationEntity referencedOn VariationOptionTable.variationId



    fun response() = VariationOptionResponse(
        id.value,
        variation.name,
        value

    )

}

