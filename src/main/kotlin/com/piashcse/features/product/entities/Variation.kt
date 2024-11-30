package com.piashcse.features.product.entities

import com.piashcse.features.product.models.variation.response.VariationResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object VariationTable: BaseIntIdTable("variation") {
    val name = varchar("name", 30)
    val subCategoryId = reference("sub_category_id", SubCategoryTable.id)
}

class VariationEntity(id: EntityID<Int>) : BaseIntEntity(id, VariationTable) {
    companion object : BaseIntEntityClass<VariationEntity>(VariationTable)

    var name by VariationTable.name
    var subCategoryId by VariationTable.subCategoryId
    private val subCategory by SubCategoryEntity referencedOn VariationTable.subCategoryId


    fun response() = VariationResponse(
        id.value,
        name
    )

}

