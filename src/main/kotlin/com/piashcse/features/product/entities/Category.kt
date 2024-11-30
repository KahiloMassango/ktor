package com.piashcse.features.product.entities

import com.piashcse.features.product.models.category.response.CategoryResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object CategoryTable : BaseIntIdTable("product_category") {
    val name = varchar("name", 25)
}

class CategoryEntity(id: EntityID<Int>) : BaseIntEntity(id, CategoryTable) {
    companion object : BaseIntEntityClass<CategoryEntity>(CategoryTable)

    var name by CategoryTable.name

    fun response() = CategoryResponse(id.value, name)
}




