package com.piashcse.features.product.entities

import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object SubCategoryTable : BaseIntIdTable("sub_category") {
    val categoryId = reference("category_id", CategoryTable.id)
    val name = varchar("name", 25)
}

class SubCategoryEntity(id: EntityID<Int>) : BaseIntEntity(id, SubCategoryTable) {
    companion object : BaseIntEntityClass<SubCategoryEntity>(SubCategoryTable)

    var categoryId by SubCategoryTable.categoryId
    var name by SubCategoryTable.name
    private val category by CategoryEntity referencedOn SubCategoryTable.categoryId

    fun response() = SubCategoryResponse(id.value, category.name, name)
}

